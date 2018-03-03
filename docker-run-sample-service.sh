#!/usr/bin/env bash

./gradlew :backend-sample-service:clean :backend-sample-service:build :backend-sample-service:buildDockerImage && \
    docker run -ti --rm -p 8081:8080 kdzido/thesis-sampleservice:latest

# -ti - interactive mode (ctrl-c) to stop container
# --rm - container will be removed after stop
