#!/usr/bin/env bash

./gradlew :eureka-server:clean :eureka-server:build && \
    docker build -t thesis/eurekaserver:latest eureka-server/build/dockerfile && \
    docker run -ti --rm -p 8761:8761 thesis/eurekaserver:latest

# -ti - interactive mode (ctrl-c) to stop container
# --rm - container will be removed after stop
