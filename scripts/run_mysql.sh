#!/usr/bin/env bash
# Create and run an Oracle MySQL instance at port 3306 without password for root user.
# This script require Docker to be setup first.
# Use of Docker Desktop: https://docs.docker.com/desktop/

docker run --name ppb-mysql \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_DATABASE=patch_place_break \
  -p 3306:3306 \
  --rm mysql:8.0.31-oracle
