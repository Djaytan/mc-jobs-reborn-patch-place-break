# Defining variables and functions here will affect all specfiles.
# Change shell options inside a function may cause different behavior,
# so it is better to set them here.

# This callback function will be invoked only once before loading specfiles.
spec_helper_precheck() {
  # Available functions: info, warn, error, abort, setenv, unsetenv
  # Available variables: VERSION, SHELL_TYPE, SHELL_VERSION
  : minimum_version "0.28.1"
}

# This callback function will be invoked after a specfile has been loaded.
spec_helper_loaded() {
  :
}

# This callback function will be invoked after core modules has been loaded.
spec_helper_configure() {
  # Available functions: import, before_each, after_each, before_all, after_all
  : import 'support/custom_matcher'
}

generate_tmp_file_with_appended_suffix() {
  local custom_suffix="$1"

  local tmp_file; tmp_file="$(mktemp)"
  local custom_file; custom_file="$(dirname "${tmp_file}")/$(basename "${tmp_file}")${custom_suffix}"
  mv "${tmp_file}" "${custom_file}"

  echo "${custom_file}"
}
