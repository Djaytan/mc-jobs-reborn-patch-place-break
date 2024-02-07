#!/usr/bin/env bash

set -Eeuo pipefail
trap 'echo Error encountered while executing the script.' ERR

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P); readonly SCRIPT_DIR

main() {
  local shellspec_args=("$@")
  local image_name="shellspec-${RANDOM}"

  # Print ANSI escape codes only if output is a TTY
  if tty --silent; then
    shellspec_args+=(--color)
  fi

  echo 'Building Docker image...'
  docker build "${SCRIPT_DIR}" --no-cache --quiet --tag "${image_name}" > /dev/null

  echo 'Running it...'
  docker run --rm --volume="/${SCRIPT_DIR}:/src" "${image_name}" "${shellspec_args[@]}"
  docker rmi "${image_name}"
}

main "$@"
