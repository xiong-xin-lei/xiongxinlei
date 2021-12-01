#!/usr/bin/env bash
set -o nounset
# ##############################################################################
# Globals, settings
# ##############################################################################
FILE_NAME="build"
FILE_VERSION="2.0.9"
BASE_DIR="$( dirname "$( readlink -f "${0}" )" )"

# ##############################################################################
# common function package
# ##############################################################################
die () {
    local status="${1}"
    shift
    error "$*"
    exit "$status"
}

error () {
    local timestamp
    timestamp="$( date +"%Y-%m-%d %T %N" )"
    echo "[${timestamp}] (${FILE_NAME}-${FILE_VERSION})ERR: $* ;"
}

info () {
    local timestamp
    timestamp="$( date +"%Y-%m-%d %T %N" )"
    echo "[${timestamp}] (${FILE_NAME}-${FILE_VERSION})INFO: $* ;"
}

# ##############################################################################
# pack function package
# ##############################################################################
pack_directory () {
    local path="${BASE_DIR}/../"

    [[ -d "${BASE_DIR}/war" ]] && {
        rm -rf "${BASE_DIR:?}/war"
    }

    [[ -d "${BASE_DIR}/sql" ]] && {
        rm -rf "${BASE_DIR}/sql"
    }

    [[ -d "${BASE_DIR}/dbscale" ]] && {
        rm -rf "${BASE_DIR}/dbscale"
    }

    mkdir "${BASE_DIR}/war" || die 30 "mkdir bin failed!"

    mkdir "${BASE_DIR}/sql"  || die 31 "mkdir sql failed!"

    cd "${path}/require/sql/" || die 32 "cd sql directory failed!"

    cp dbscale-structure.sql "${BASE_DIR}/sql/dbscale-structure.sql" || die 33 "copy dbscale-structure.sql failed!"
    cp dbscale-dataonly.sql "${BASE_DIR}/sql/dbscale-dataonly.sql" || die 34 "copy dbscale-dataonly.sql failed!"

    cd "${path}/dbscale-parent" || die 35 "cd dbscale-parent failed!"

    sh build.sh "${BASE_DIR}/war" || die 36 "build war failed!"

    cd "${path}/require/cfg/" || die 37 "cd cfg directory failed!"

    cp -r dbscale "${BASE_DIR}" || die 38 "copy dbscale config directory failed!"

}

usage (){
    echo "Usage: "
    echo "  $0 -u <upload_flag>"
    echo "      options: "
    echo "          -u upload_flag: enable upload image to harbor"
    echo "              certs_directoy default: 0"
}

# ##############################################################################
# The main() function is called at the action function.
# ##############################################################################
UPLOAD_FLAG=0
PROJECT="dbscale"
REPO_NAME="web-manager"
BRANCH="release-v3.0"
REPO_VERSION="$( cat "${BASE_DIR}/VERSION" )" || die 10 "get REPO_VERSION failed!"

# get options
while getopts ":u:h:" arg; do
    case $arg in
        u) # Specify upload_flag.
            UPLOAD_FLAG="${OPTARG}"
            ;;
        h | *) # Display help.
            usage
            exit 0
            ;;
    esac
done

CUR_BRANCH="$( git rev-parse --abbrev-ref HEAD )"
if [[ "${BRANCH}" == "${CUR_BRANCH}" ]]; then
    # sync code
    git pull origin "${BRANCH}" || die 11 "git pull ${BRANCH} failed!"
    COMMIT_ID="$( git rev-parse --short HEAD )" || die 11 "get commit id failed!"
else
    die 12 "current branch(${CUR_BRANCH}) is not ${BRANCH} !Please checkout to ${CUR_BRANCH}"
fi
IMAGE_NAME="${PROJECT}/${REPO_NAME}:${REPO_VERSION}-${COMMIT_ID}"

info "main" "Starting pack directory"

pack_directory

info "Pack directory done !"

info "Starting build image"

cd "${BASE_DIR}" || die 13 "cd ${BASE_DIR} failed!"
if printenv http_proxy; then
    docker build --rm=true --network=host -t "${IMAGE_NAME}"  --build-arg http_proxy="$( printenv http_proxy )" --build-arg https_proxy="$( printenv https_proxy )" . || {
        die 14 "build docker image(${IMAGE_NAME}) files failed!"
    }
else
    docker build --rm=true --network=host -t "${IMAGE_NAME}" . || {
        die 15 "build docker image(${IMAGE_NAME}) files failed!"
    }
fi

info "Build image(${IMAGE_NAME}) done !!!"

DEPLOY_REPO_DIR="/root/dbscale-kube-deploy"
cd "${DEPLOY_REPO_DIR}" || die 15 "cd ${DEPLOY_REPO_DIR} failed!"
CUR_BRANCH="$( git rev-parse --abbrev-ref HEAD )"
if [[ "${BRANCH}" == "${CUR_BRANCH}" ]]; then
    # sync code
    git pull origin "${BRANCH}" || die 11 "git pull ${BRANCH} failed!"
else
    die 12 "dbscale-kube-deploy current branch(${CUR_BRANCH}) is not ${BRANCH} !Please checkout to ${CUR_BRANCH}"
fi
RELEASES_FILE="${DEPLOY_REPO_DIR}/manager/release/${REPO_NAME}"

info "Starting push ${RELEASES_FILE}"
local_image_id=$( docker images --no-trunc "${IMAGE_NAME}" --format "{{.ID}}" | awk -F: '{print $2}' ) || die 16 "get docker image(${IMAGE_NAME}) ID failed!"
cat << EOF > "${RELEASES_FILE}"
checksums:
  amd64:
    ${REPO_VERSION}-${COMMIT_ID}: ${local_image_id}
EOF
git add "${RELEASES_FILE}" || die 17 "git add failed!"
git commit -m "update ${RELEASES_FILE}" || die 18 "git commit failed!"
git push || die 19 "git push failed!"

info "Push ${RELEASES_FILE} done !!!"

if [[ "${UPLOAD_FLAG}" == 1 ]]; then
    info "Starting upload image(${IMAGE_NAME}) to docker.io/${PROJECT}"

    docker login || die 20 "docker login failed!"
    docker push "${IMAGE_NAME}" || die 21 "docker push failed!"

    info "upload image(${IMAGE_NAME}) to docker.io/${PROJECT} done !!!"

    local_harbor="harbor-200-248.dbscale.dev:28083"

    info "Starting upload image(${IMAGE_NAME}) to ${local_harbor}/${PROJECT}"

    docker login "${local_harbor}" || die 22 "docker login failed!"
    docker tag "${IMAGE_NAME}" "${local_harbor}/${IMAGE_NAME}" || die 23 "docker login failed!"
    docker push "${local_harbor}/${IMAGE_NAME}" || die 24 "docker push failed!"

    info "upload image(${IMAGE_NAME}) to ${local_harbor}/${PROJECT} done !!!"
fi
