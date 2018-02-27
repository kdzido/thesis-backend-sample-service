#!/usr/bin/env bash

./gradlew :eureka-server:clean :eureka-server:build :eureka-server:buildDockerImage && \
    docker run -ti --rm -p 8761:8761 kdzido/eurekaserver:latest

# -ti - interactive mode (ctrl-c) to stop container
# --rm - container will be removed after stop
