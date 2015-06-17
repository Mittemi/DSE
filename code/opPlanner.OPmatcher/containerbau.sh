#!/bin/bash

echo "remove previous container if exists..."
docker rm -f opmatcher

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t opmatcher .

docker run -d -p 9001:8080  --name opmatcher opmatcher --link klinisys:klinisys --link notifier:notifier --link reservation:reservation --link apigateway:apigateway