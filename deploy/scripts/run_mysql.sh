#!/usr/bin/env bash

docker run --name ppb-mysql \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_DATABASE=patch_place_break \
  -p 3306:3306 \
  --rm mysql:8.0.31-oracle
