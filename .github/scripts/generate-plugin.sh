#!/usr/bin/env bash

set -Eeuo pipefail
trap 'echo Error encountered while executing the script.' ERR

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P)
ROOT_REPOSITORY_DIR="${SCRIPT_DIR}/.."

NEW_VERSION="$1"
DEV_VERSION='0.0.1-DEV-SNAPSHOT'

echo "Generating plugin file for version ${NEW_VERSION}..."

cd "${ROOT_REPOSITORY_DIR}"

mvn versions:set -DnewVersion="${NEW_VERSION}" --batch-mode --errors > /dev/null
mvn clean package --activate-profiles fast --batch-mode --errors --strict-checksums

PLUGIN_FILE_NAME="$(mvn help:evaluate -Dexpression=spigotPlugin.finalName -DforceStdout --projects :spigot-plugin --quiet)"
PLUGIN_FILE="${ROOT_REPOSITORY_DIR}/src/spigot-plugin/target/${PLUGIN_FILE_NAME}.jar"

if [[ ! -f "${PLUGIN_FILE}" ]]; then
  echo 'The plugin file does not exist or is invalid'
  echo 'Aborting...'
  exit 1
fi

echo "The generated plugin file: '${PLUGIN_FILE}'"

mvn versions:set -DnewVersion="${DEV_VERSION}" --batch-mode --errors > /dev/null
