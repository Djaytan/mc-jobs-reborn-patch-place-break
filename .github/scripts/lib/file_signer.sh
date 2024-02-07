#!/usr/bin/env bash

#####
# Sign a set of files present in a given directory.
#
# The following files will not be signed:
# * The signature ones
# * The already signed ones
#
# Parameters:
# * $1 - The directory containing the files to be signed
# * $2 - The directory where the signature files must be created
#####
sign_files() {
  local files_dir="$1"
  local signatures_dir="$2"

  for i in "${files_dir}"/*; do
    local file; file="${files_dir}/$(basename "${i}")"
    sign_file "${file}" "${signatures_dir}"
  done
}

#####
# Sign a given file.
#
# If the file is a signature one, or if its associated signatures files already exist
# in the signature directory, then the file will not be signed.
#
# Parameters:
# * $1 - The file to be signed
# * $2 - The directory where the signature files must be created
#####
sign_file() {
  local file="$1"
  local signatures_dir="$2"

  if [[ ! -f "${file}" ]]; then
    echo "The specified file '${file}' does not exist or is not an ordinary file"
    return 1
  fi

  if [[ ! -d "${signatures_dir}" ]]; then
    echo "The specified signatures directory '${signatures_dir}' does not exist or is not a repository"
    return 1
  fi

  local file_name; file_name="$(basename "${file}")"
  local sig_file="${signatures_dir}/${file_name}-keyless.sig"
  local cert_file="${signatures_dir}/${file_name}-keyless.pem"

  if [[ "${file}" =~ -keyless\.(sig|pem)$ ]]; then
    echo "The file is a signature one ('${file}')"
    echo 'Cancelling signature...'
    return 0
  fi

  echo "The file '${file}' is going to be signed..."

  cosign sign-blob "${file}" --yes \
    --output-signature="${sig_file}" \
    --output-certificate="${cert_file}"
}

#####
# Verify the signature of the provided file.
#
# Parameters:
# * $1 - The file for which to verify the signature
# * $2 - The signature file
# * $3 - The certificate file of the signature
# * $4 - The expected certificate identity
# * $5 - The expected OpenID Connect (OIDC) issuer
#####
verify_file_signature() {
  local file="$1"
  local sig_file="$2"
  local cert_file="$3"
  local cert_id="$4"
  local cert_oidc_issuer="$5"

  if [[ ! -f "${file}" ]]; then
    echo "The specified file '${file}' does not exist or is not an ordinary file"
    return 1
  fi

  if [[ ! -f "${sig_file}" ]]; then
    echo "The specified signature file '${sig_file}' does not exist or is not an ordinary file"
    return 1
  fi

  if [[ ! -f "${cert_file}" ]]; then
    echo "The specified certificate file '${cert_file}' does not exist or is not an ordinary file"
    return 1
  fi

  if [[ -z "${cert_id}" ]]; then
    echo "The certificate identity is mandatory and thus can not be empty"
    return 1
  fi

  if [[ -z "${cert_oidc_issuer}" ]]; then
    echo "The certificate OIDC issuer is mandatory and thus can not be empty"
    return 1
  fi

  cosign verify-blob "${file}" \
    --signature="${sig_file}" \
    --certificate="${cert_file}" \
    --certificate-identity="${cert_id}" \
    --certificate-oidc-issuer="${cert_oidc_issuer}"
}
