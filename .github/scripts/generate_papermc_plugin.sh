#!/usr/bin/env bash
#
# Generate the PaperMC plugin of the JobsReborn place-break patch.

set -Eeuo pipefail
trap 'echo "Error encountered while executing the script." >&2' ERR

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P); readonly SCRIPT_DIR
ROOT_REPOSITORY_DIR="$(realpath "${SCRIPT_DIR}/../..")"; readonly ROOT_REPOSITORY_DIR

mvnw="${ROOT_REPOSITORY_DIR}/mvnw"

readonly NEW_VERSION="$1"
readonly DEV_VERSION='0.0.1-DEV-SNAPSHOT'

main() {
  echo "Generating plugin file for version ${NEW_VERSION}..."

  cd "${ROOT_REPOSITORY_DIR}"

  $mvnw versions:set -DnewVersion="${NEW_VERSION}"
  $mvnw clean package

  local plugin_file_name
  plugin_file_name="$(mvn help:evaluate -Dexpression=paperPlugin.finalName -DforceStdout --projects :paper-plugin --quiet)"

  local plugin_file="${ROOT_REPOSITORY_DIR}/src/paper-plugin/target/${plugin_file_name}.jar"

  if [[ ! -f "${plugin_file}" ]]; then
      echo 'Error: the plugin file does not exist or is invalid' >&2
    exit 1
  fi

  echo "The generated plugin file: '${plugin_file}'"

  $mvnw versions:set -DnewVersion="${DEV_VERSION}"
}

main
