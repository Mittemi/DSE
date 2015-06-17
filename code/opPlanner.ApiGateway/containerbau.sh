#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo "ERROR - Please provide min. one argument (HOST_IP)"
	exit 1
fi
echo "remove previous container if exists..."
docker rm -f apigateway

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t apigateway .
echo "HOST_IP=$1"
docker run -d -p 8080:8080 -e "HOST_IP=$1" --name apigateway apigateway 