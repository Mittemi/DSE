#!/bin/bash
if [ "$#" -ne 2 ]; then
	echo "ERROR - Please provide min. two arguments (HOST_IP, POSTGRES_IP)"
	exit 1
fi
echo "remove previous container if exists..."
docker rm -f klinisys

echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t klinisys .
echo "HOST_IP=$1"
echo "POSTGRES_IP=$2"
docker run -d -p 9000:8080 -e "HOST_IP=$1" -e "POSTGRES_IP=$2" --name klinisys klinisys