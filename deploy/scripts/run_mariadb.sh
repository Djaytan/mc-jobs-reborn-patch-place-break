#!/usr/bin/env bash

docker run --name ppb-mariadb \
  -e MARIADB_ALLOW_EMPTY_ROOT_PASSWORD=yes \
  -e MARIADB_DATABASE=patch_place_break \
  -p 3306:3306 \
  --rm mariadb:10.10.2-jammy
