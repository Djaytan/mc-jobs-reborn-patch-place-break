FROM gcr.io/projectsigstore/cosign:v2.2.3@sha256:8fc9cad121611e8479f65f79f2e5bea58949e8a87ffac2a42cb99cf0ff079ba7 AS cosign-bin

FROM registry-1.docker.io/shellspec/shellspec:0.28.1-kcov@sha256:39346b73db30d00001355a3e6e3138396a20d2d90245a37c619520630b8a9b3e

COPY --from=cosign-bin /ko-app/cosign /usr/local/bin/cosign
