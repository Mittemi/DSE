#!/bin/bash

echo "remove previous container if exists..."
docker rm -f notifier

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t notifier .

docker run -d -p 9003:8080  --name notifier notifier