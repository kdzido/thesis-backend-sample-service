#!/bin/sh
echo "Starting Eureka service discovery server"
echo "------------------------------"
java -Djava.security.egd=file:/dev/./urandom -jar /usr/local/eureka-server/@springBootJar@
