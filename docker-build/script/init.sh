#!/bin/bash

set -o nounset
# ##############################################################################
# Globals, settings
# ##############################################################################
FILE_NAME="init"
VERSION="1.0.1"

db_name="${1}"
db_host="${2}"
db_port="${3}"
db_auth="${4}"
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
    echo "[${timestamp}] (${FILE_NAME}-${VERSION})ERR: $* ;"
}

info () {
    local timestamp
    timestamp="$( date +"%Y-%m-%d %T %N" )"
    echo "[${timestamp}] (${FILE_NAME}-${VERSION})INFO: $* ;"
}

installed () {
    command -v "$1" >/dev/null 2>&1
}
# ##############################################################################
# The main() function is called at the action function.
# ##############################################################################
main(){
    installed mysql || die 10 "not found mysql client tools!"

    local auth_dencrypt
    auth_dencrypt="$( openssl base64 -d <<< "${db_auth}" )"
    test -n "${auth_dencrypt}" || die 11 "dencode auth failed!"

    local db_user="${auth_dencrypt%%:*}"
    local db_pwd="${auth_dencrypt##*:}"

    local resp
    resp="$( mysql -u"${db_user}" -p"${db_pwd}" -h"${db_host}" -P"${db_port}" --disable-column-names -B -e "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='dbscale';")"
    info "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME=dbscale return ${resp}"
    [[ "${resp}" -ne 0 ]] || {
        mysql -u"${db_user}" -p"${db_pwd}" -h"${db_host}" -P"${db_port}" -e "create database IF NOT EXISTS ${db_name};" || die 12 "mysql create database ${db_name} failed!"
        mysql -u"${db_user}" -p"${db_pwd}" -h"${db_host}" -P"${db_port}" -D "${db_name}" < "/opt/web-manager/sql/dbscale-structure.sql" || die 13 "mysql source dbscale-structure.sql failed!"
        mysql -u"${db_user}" -p"${db_pwd}" -h"${db_host}" -P"${db_port}" -D "${db_name}" -f < "/opt/web-manager/sql/dbscale-dataonly.sql" || die 14 "mysql source dbscale-dataonly.sql failed!"
    }
}

main "${@:-""}"
