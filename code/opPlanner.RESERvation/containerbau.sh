#!/bin/bash
if [ "$#" -ne 2 ]; then
	echo "ERROR - Please provide min. two arguments (HOST_IP, MONGO_IP)"
	exit 1
fi
echo "remove previous container if exists..."
docker rm -f reservation

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t reservation .
echo "HOST_IP=$1"
echo "MONGO_IP=$2"
docker run -d -p 9002:8080 -e "HOST_IP=$1" -e "MONGO_IP=$2" --name reservation reservation