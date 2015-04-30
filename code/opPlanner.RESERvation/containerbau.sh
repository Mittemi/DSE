#!/bin/bash

echo "remove previous container if exists..."
docker rm -f reservation

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t reservation .

docker run -d -p 9002:8080  --name reservation reservation