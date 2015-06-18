#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo "ERROR - Please provide min. one argument (HOST_IP)"
	exit 1
fi
echo "remove previous container if exists..."
docker rm -f klinisys

pattern="s/127.0.0.1/$1/g"
echo "Pattern: $pattern"
cp src/main/resources/opPlanner.yml src/main/resources/opPlanner_tmp.yml
cp src/main/resources/application.yml src/main/resources/application_tmp.yml
cat src/main/resources/opPlanner.yml | \
sed -e $pattern > tmp
mv tmp src/main/resources/opPlanner.yml

pattern2="s/127.0.0.1/$1/g"
cat src/main/resources/application.yml | \
sed -e $pattern2 > tmp
mv tmp src/main/resources/application.yml
echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t klinisys .
echo "HOST_IP=$1"
docker run -d -p 9000:9000 --name klinisys klinisys
rm src/main/resources/opPlanner.yml
mv src/main/resources/opPlanner_tmp.yml src/main/resources/opPlanner.yml 
rm src/main/resources/application.yml
mv src/main/resources/application_tmp.yml src/main/resources/application.yml