Describe 'lib/file_signer.sh'
  setup() {
    test_nominal_file="$(mktemp)" && \
      readonly test_nominal_file && \
      echo 'test-file-content' > "${test_nominal_file}"

    test_nominal_sign_dir="$(mktemp -d)" && readonly test_nominal_sign_dir
  }
  teardown() {
    rm "${test_nominal_file}"
    rm -r "${test_nominal_sign_dir}"
  }
  BeforeEach 'setup'
  AfterEach 'teardown'

  Include lib/file_signer.sh

  #####
  # Mock the cosign CLI.
  #
  # This is useful since testing the signing process in an automated way with OIDC tokens isn't
  # trivial outside CI context (e.g. localdev).
  #
  # In fact, an alternative way has been found for testing the signature process through
  # the cosign CLI. Instead of providing an OIDC token and thus generating a signature in
  # a keyless mode as it is the case by default, we fallback to a basic simple signature mode
  # with a pre-generated key-pair stored in the Git repository. We don't care about leaking
  # a private key since it will used for testing purposes only. The drawback of this method is that
  # we are not testing the signature process exactly as it would be in real life scenarios,
  # but it is counterbalanced by the localdev testing experience working out-of-the-box.
  # This benefit is something hard to setup with keyless mode. At least, this is the case for now,
  # but since the cosign CLI is pretty recent, we can hope for some evolutions on the keyless mode
  # testability in the coming months/years.
  #
  # If we would really like to reproduce exact real life signing scenarios locally, it would require
  # the developer to setup an environment variable in his machine with a GitHub PAT in order to
  # generate an OIDC ID token from the GitHub OIDC issuer. That's less convenient for sure.
  #
  # The better trade-off found is the following one in fact: falling back to a basic signature mode
  # only when outside a GitHub Actions context (or any other CI environment supported by cosign
  # out-of-the-box). By doing so, we are sure to test real life scenarios before integrating any
  # change while preserving an easy to run test suite.
  #####
  cosign() {
    if [[ "${CI}" == 'true' ]]; then
      # We test real life signing scenarios when executing tests in CI
      @cosign "$@"
    else
      # We fallback to a basic signature mode outside CI
      # Redirecting stderr output to /dev/null is required to avoid warning messages
      echo -n '' | @cosign "$@" --key=spec/resources/cosign.key 2> /dev/null
      # Generate a fake certificate file to make test green
      touch "${test_nominal_sign_dir}/$(basename "${test_nominal_file}")-keyless.pem"
    fi
  }

  Describe 'sign_file()'
    It 'Signing a nominal file shall success'
      File signature_file="${test_nominal_sign_dir}/$(basename "${test_nominal_file}")-keyless.sig"
      File certificate_file="${test_nominal_sign_dir}/$(basename "${test_nominal_file}")-keyless.pem"

      When call sign_file "${test_nominal_file}" "${test_nominal_sign_dir}"
      The first line of output should equal "The file '${test_nominal_file}' is going to be signed..."
      The file signature_file should not be empty file
      The path certificate_file should be a file
      The status should be success
    End

    Describe 'Signing while inputs are invalid shall fail'
      It 'e.g. without any argument specified'
        When call sign_file
        The output should equal "The specified file '' does not exist or is not an ordinary file"
        The status should be failure
      End

      Describe 'e.g. with first argument'
        It 'empty'
          When call sign_file '' "${test_nominal_sign_dir}"
          The output should equal "The specified file '' does not exist or is not an ordinary file"
          The status should be failure
        End

        It 'being a non-existing file'
          When call sign_file 'non_existing_file' "${test_nominal_sign_dir}"
          The output should equal "The specified file 'non_existing_file' does not exist or is not an ordinary file"
          The status should be failure
        End

        It 'not being an ordinary file'
          unexpected_directory="$(mktemp -d)"; readonly unexpected_directory

          When call sign_file "${unexpected_directory}" "${test_nominal_sign_dir}"
          The output should equal "The specified file '${unexpected_directory}' does not exist or is not an ordinary file"
          The status should be failure
        End
      End

      Describe 'e.g. with second argument'
        It 'empty'
          When call sign_file "${test_nominal_file}"
          The output should equal "The specified signatures directory '' does not exist or is not a repository"
          The status should be failure
        End

        It 'being a non-existing folder'
          When call sign_file "${test_nominal_file}" 'non_existing_folder'
          The output should equal "The specified signatures directory 'non_existing_folder' does not exist or is not a repository"
          The status should be failure
        End

        It 'not being a directory'
          unexpected_ordinary_file="$(mktemp)"; readonly unexpected_ordinary_file

          When call sign_file "${test_nominal_file}" "${unexpected_ordinary_file}"
          The output should equal "The specified signatures directory '${unexpected_ordinary_file}' does not exist or is not a repository"
          The status should be failure
        End
      End
    End

    # We assume it will never be expected to sign a signature file.
    # Making this assumption allows us to avoid signing signature files like it can be the case
    # when iterating over a bunch of files with signature contained among them
    # (e.g. in GitHub release assets).
    Describe 'Signing a signature file shall not conclude nor fail'
      It 'e.g. signing a *-keyless.sig file'
        test_sig_file="$(generate_tmp_file_with_appended_suffix '-keyless.sig')"; readonly test_sig_file

        When call sign_file "${test_sig_file}" "${test_nominal_sign_dir}"
        The first line of output should equal "The file is a signature one ('${test_sig_file}')"
        The second line of output should equal 'Cancelling signature...'
        The status should be success
      End

      It 'e.g. signing a *-keyless.pem file'
        test_cert_file="$(generate_tmp_file_with_appended_suffix '-keyless.pem')"; readonly test_cert_file

        When call sign_file "${test_cert_file}" "${test_nominal_sign_dir}"
        The first line of output should equal "The file is a signature one ('${test_cert_file}')"
        The second line of output should equal 'Cancelling signature...'
        The status should be success
      End
    End

    It 'Signing an already signed file shall override the existing signature'
      test_sign_file="${test_nominal_sign_dir}/$(basename "${test_nominal_file}")-keyless.sig" && \
        readonly test_sign_file && \
        touch "${test_sign_file}"
      test_cert_file="${test_nominal_sign_dir}/$(basename "${test_nominal_file}")-keyless.pem" && \
        readonly test_cert_file && \
        touch "${test_cert_file}"

      File signature_file="${test_sign_file}"
      File certificate_file="${test_cert_file}"

      When call sign_file "${test_nominal_file}" "${test_nominal_sign_dir}"
      The first line of output should equal "The file '${test_nominal_file}' is going to be signed..."
      The file signature_file should not be empty file
      The path certificate_file should be a file
      The status should be success
    End
  End
End
