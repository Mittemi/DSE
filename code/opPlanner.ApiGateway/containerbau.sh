#!/bin/bash

echo "remove previous container if exists..."
docker rm -f apigateway

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t apigateway .

docker run -d -p 8080:8080  --name apigateway apigateway