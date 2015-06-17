#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo "ERROR - Please provide min. one argument (HOST_IP)"
	exit 1
fi
echo "remove previous container if exists..."
docker rm -f reservation

pattern="s/127.0.0.1/$1/g"
echo "Pattern: $pattern"
cp src/main/resources/reservation.yml src/main/resources/reservation_tmp.yml
cat src/main/resources/reservation.yml | \
sed -e $pattern > src/main/resources/reservation.yml
echo "rebuilt app Dockerfile"
gradle build -x test
docker build -t reservation .
echo "HOST_IP=$1"
docker run -d -p 9002:8080 --name reservation reservation
rm src/main/resources/reservation.yml
mv src/main/resources/reservation_tmp.yml src/main/resources/reservation.yml 