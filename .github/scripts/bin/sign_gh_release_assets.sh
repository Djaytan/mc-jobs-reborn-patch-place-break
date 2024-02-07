#!/usr/bin/env bash
# The GITHUB_TOKEN environment variable must be defined prior calling this script

set -Eeuo pipefail
trap 'echo Error encountered while executing the script.' ERR

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P); readonly SCRIPT_DIR
LIB_FOLDER="${SCRIPT_DIR}/../lib"; readonly LIB_FOLDER
GH_REPOSITORY_URL="$1"; readonly GH_REPOSITORY_URL
GIT_TAG_VERSION="$2"; readonly GIT_TAG_VERSION

source "${LIB_FOLDER}/file_signer.sh"

validate_inputs() {
  echo 'Checking GitHub repository URL...'
  if [[ $(gh repo view "${GH_REPOSITORY_URL}" 2>/dev/null) != 0 ]]; then
    echo "Error: the GitHub repository URL '${GH_REPOSITORY_URL}' is invalid"
  fi

  echo 'Checkout Git tag version...'
  # The following RegEx is based on https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string
  # Note the 'v' character at the beginning since we are manipulating Git tags following the format "v<semver>"
  if [[ ! ("${GIT_TAG_VERSION}" =~ ^v(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(-((0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(\.(0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(\+([0-9a-zA-Z-]+(\.[0-9a-zA-Z-]+)*))?$) ]]; then
    echo "Error: invalid Git tag version '${GIT_TAG_VERSION}'"
    exit 1
  fi

  echo 'Checking GitHub release existence...'
  if [[ -z $(gh release list --repo "${GH_REPOSITORY_URL}" --json tagName --jq ".[].tagName | select(. == \"${GIT_TAG_VERSION}\")" 2>/dev/null) ]]; then
    echo "Error: no GitHub release associated to the tag name '${GIT_TAG_VERSION}' found in the repository '${GH_REPOSITORY_URL}'"
    exit 1
  fi
}

main() {
  validate_inputs

  local assets_dir; assets_dir="$(mktemp -d)"
  local signatures_dir; signatures_dir="$(mktemp -d)"

  echo "Downloading assets from GitHub release ${GIT_TAG_VERSION}..."
  gh release download "${GIT_TAG_VERSION}" --repo "${GH_REPOSITORY_URL}" --dir "${assets_dir}"

  sign_files "${assets_dir}" "${signatures_dir}"

  if [[ -n "$(ls --almost-all "${signatures_dir}" 2>/dev/null)" ]]; then
    echo "Uploading signature files in the GitHub release ${GIT_TAG_VERSION}..."
    gh release upload "${GIT_TAG_VERSION}" "${signatures_dir}"/* --repo "${GH_REPOSITORY_URL}"
  else
    echo 'No signature file to be uploaded in the GitHub release'
  fi
}

main
