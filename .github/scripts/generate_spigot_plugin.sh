#!/usr/bin/env bash
#
# Generate the Spigot plugin of the JobsReborn place-break patch.

set -Eeuo pipefail
trap 'echo "Error encountered while executing the script." >&2' ERR

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P); readonly SCRIPT_DIR
ROOT_REPOSITORY_DIR="$(realpath "${SCRIPT_DIR}/../..")"; readonly ROOT_REPOSITORY_DIR

readonly NEW_VERSION="$1"
readonly DEV_VERSION='0.0.1-DEV-SNAPSHOT'

main() {
  echo "Generating plugin file for version ${NEW_VERSION}..."

  cd "${ROOT_REPOSITORY_DIR}"

  mvn versions:set -DnewVersion="${NEW_VERSION}" --batch-mode --errors
  mvn clean package --activate-profiles fast --batch-mode --errors --strict-checksums

  local plugin_file_name
  plugin_file_name="$(mvn help:evaluate -Dexpression=spigotPlugin.finalName -DforceStdout --projects :spigot-plugin --quiet)"

  local plugin_file="${ROOT_REPOSITORY_DIR}/src/spigot-plugin/target/${plugin_file_name}.jar"

  if [[ ! -f "${plugin_file}" ]]; then
      echo 'Error: the plugin file does not exist or is invalid' >&2
    exit 1
  fi

  echo "The generated plugin file: '${plugin_file}'"

  mvn versions:set -DnewVersion="${DEV_VERSION}" --batch-mode --errors
}

main
