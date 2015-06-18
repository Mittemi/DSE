#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo "ERROR - Please provide min. one argument (HOST_IP)"
	exit 1
fi
echo "remove previous container if exists..."
docker rm -f opmatcher

pattern="s/127.0.0.1/$1/g"
echo "Pattern: $pattern"
cp src/main/resources/opPlanner.yml src/main/resources/opPlanner_tmp.yml
cat src/main/resources/opPlanner.yml | \
sed -e $pattern > tmp
mv tmp src/main/resources/opPlanner.yml
echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t opmatcher .
echo "HOST_IP=$1"
docker run -d -p 9001:9001 --name opmatcher opmatcher 
rm src/main/resources/opPlanner.yml
mv src/main/resources/opPlanner_tmp.yml src/main/resources/opPlanner.yml 