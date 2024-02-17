#!/usr/bin/env bash
#
# Sign assets of a given GitHub release.
#
# Globals:
#   GITHUB_TOKEN: the GitHub access token with write permissions for "contents" and "id-token".
#   CERTIFICATE_IDENTITY: the certificate identity used for verifying the files' signatures.
#   CERTIFICATE_OIDC_ISSUER: the certificate OIDC issuer of the identity for verifying the files' signatures.
#
# Arguments:
#   $1: the Git tag version for identifying the GitHub release having assets to be signed.

set -Eeuo pipefail
trap 'echo "Error encountered while executing the script." >&2' ERR

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P); readonly SCRIPT_DIR
LIB_FOLDER="${SCRIPT_DIR}/lib"; readonly LIB_FOLDER

readonly GIT_TAG_VERSION="$1"

source "${LIB_FOLDER}/sigstore_file_signer.sh"

validate_git_tag_version() {
  echo 'Checkout Git tag version...'
  if ! [[ "${GIT_TAG_VERSION}" =~ ^v[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
    echo "Error: invalid Git tag version '${GIT_TAG_VERSION}'" >&2
    exit 1
  fi

  echo 'Checking GitHub release existence...'
  if [[ -z $(gh release list --json tagName --jq ".[].tagName | select(. == \"${GIT_TAG_VERSION}\")" 2>/dev/null) ]]; then
    echo "Error: no GitHub release associated to the tag name '${GIT_TAG_VERSION}' found" >&2
    exit 1
  fi
}

main() {
  if [[ -z "${GITHUB_TOKEN}" || -z "${CERTIFICATE_IDENTITY}" || -z "${CERTIFICATE_OIDC_ISSUER}" ]]; then
    echo 'Error: the variables GITHUB_TOKEN, CERTIFICATE_IDENTITY and CERTIFICATE_OIDC_ISSUER must be defined' >&2
    return 1
  fi

  validate_git_tag_version

  local assets_dir; assets_dir="$(mktemp -d)"
  local signatures_dir; signatures_dir="$(mktemp -d)"

  echo "Downloading assets from GitHub release ${GIT_TAG_VERSION}..."
  gh release download "${GIT_TAG_VERSION}" --dir "${assets_dir}"

  sign_files "${assets_dir}" "${signatures_dir}" "${CERTIFICATE_IDENTITY}" "${CERTIFICATE_OIDC_ISSUER}"

  if [[ -n "$(ls --almost-all "${signatures_dir}" 2>/dev/null)" ]]; then
    echo "Uploading signature files in the GitHub release ${GIT_TAG_VERSION}..."
    gh release upload "${GIT_TAG_VERSION}" "${signatures_dir}"/*
    echo 'The signature files have been uploaded successfully'
  else
    echo 'Error: no signature file to be uploaded in the GitHub release' >&2
    return 1
  fi
}

main
