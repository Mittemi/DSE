FROM java:8
MAINTAINER dse@kraenkl.com
EXPOSE 8080
WORKDIR /data/
ADD build/libs/opPlanner.ApiGateway-0.0.1-SNAPSHOT.jar /data/apigateway.jar
CMD java -jar apigateway.jar
