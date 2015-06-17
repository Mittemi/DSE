FROM dockerfile/java:oracle-java8
MAINTAINER dse@kraenkl.com
EXPOSE 8080
ADD build/libs/opPlanner.ApiGateway-0.0.1-SNAPSHOT.jar /data/apigateway.jar
ENV HOST_IP=127.0.0.1
ENV application.klinisys.ipOrHostname=asdf
CMD java -jar apigateway.jar --application.klinisys.ipOrHostname=$HOST_IP