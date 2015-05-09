#!/bin/bash

echo "remove previous container if exists..."
docker rm -f klinisys

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t klinisys .

docker run -d -p 9000:8080  --name klinisys klinisys