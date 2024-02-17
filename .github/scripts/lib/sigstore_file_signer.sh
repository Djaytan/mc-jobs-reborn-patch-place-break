#!/usr/bin/env bash
#
# Signs files through Sigstore by relying on the Cosign client.

#####
# Sign a set of files present in a given directory.
#
# A verification against the Sigstore system is performed right after the signing to ensure that
# the signature is valid.
#
# Note: the signature files (*-keyless.sig & *-keyless.pem ones) will not be signed.
#
# Arguments:
#   $1: the directory containing the files to be signed.
#   $2: the directory where the signature files must be created.
#   $3: the expected certificate identity.
#   $4: the expected OpenID Connect (OIDC) issuer.
#####
sign_files() {
  local files_dir="$1"
  local signatures_dir="$2"
  local cert_id="$3"
  local cert_oidc_issuer="$4"

  for i in "${files_dir}"/*; do
    local file; file="${files_dir}/$(basename "${i}")"
    sign_file "${file}" "${signatures_dir}" "${cert_id}" "${cert_oidc_issuer}"
  done
}

#####
# Sign the specified file.
#
# A verification against the Sigstore system is performed right after the signing to ensure that
# the signature is valid.
#
# Note: the signature files (*-keyless.sig & *-keyless.pem ones) will not be signed.
#
# Arguments:
#   $1: the file to be signed.
#   $2: the directory where the signature files must be created.
#   $3: the expected certificate identity.
#   $4: the expected OpenID Connect (OIDC) issuer.
#####
sign_file() {
  local file="$1"
  local signatures_dir="$2"
  local cert_id="$3"
  local cert_oidc_issuer="$4"

  if [[ ! -f "${file}" ]]; then
    echo "Error: the specified file '${file}' does not exist or is not an ordinary file" >&2
    return 1
  fi

  if [[ ! -d "${signatures_dir}" ]]; then
    echo "Error: the specified signatures directory '${signatures_dir}' does not exist or is not a repository" >&2
    return 1
  fi

  local file_name; file_name="$(basename "${file}")"
  local sig_file="${signatures_dir}/${file_name}-keyless.sig"
  local cert_file="${signatures_dir}/${file_name}-keyless.pem"

  if [[ "${file}" =~ -keyless\.(sig|pem)$ ]]; then
    echo "Warning: the file to be signed is a signature one ('${file}'), skipping..."
    return 0
  fi

  echo "The file '${file}' is going to be signed..."

  cosign sign-blob "${file}" --yes \
    --output-signature="${sig_file}" \
    --output-certificate="${cert_file}"

  cosign verify-blob "${file}" \
      --signature="${sig_file}" \
      --certificate="${cert_file}" \
      --certificate-identity="${cert_id}" \
      --certificate-oidc-issuer="${cert_oidc_issuer}"
}
