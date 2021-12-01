#!/bin/bash

set -o nounset

DIR="$( readlink -f "$0" )"
BASE_DIR="$( dirname "${DIR}" )"
installed () {
    command -v "$1" >/dev/null 2>&1
}

_warn () {
    echo "$@" >&2
}

die () {
    local status="$1"
    shift
    _warn "$@"
    exit "$status"
}


ouput="$1"
targetpath="${BASE_DIR}/dbscale-modules/dbscale/target"
tar_file="dbscale.war"

installed mvn || die 3 "not installed maven"

mvn clean

mvn install && {
    mv "${targetpath}/${tar_file}" "${ouput}"
}
