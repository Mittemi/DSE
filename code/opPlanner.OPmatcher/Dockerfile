FROM dockerfile/java:oracle-java8
MAINTAINER dse@kraenkl.com
EXPOSE 8080
ADD build/libs/opPlanner.OPmatcher-0.0.1-SNAPSHOT.jar /data/opmatcher.jar
ENV HOST_IP=127.0.0.1
ENV MONGO_IP=127.0.0.1
CMD java -jar opmatcher.jar