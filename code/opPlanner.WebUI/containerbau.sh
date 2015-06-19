#!/bin/bash
echo "remove previous container if exists..."
docker rm -f webserver
echo "NOW INSTALLING LATEST BOWER PACKAGES"
bower install
echo "rebuilt app Dockerfile"
docker build -t webserver .
docker run -d -p 80:80  --name webserver webserver