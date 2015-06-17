FROM dockerfile/java:oracle-java8
MAINTAINER dse@kraenkl.com
EXPOSE 8080
ADD build/libs/opPlanner.KLINIsys-0.0.1-SNAPSHOT.jar /data/klinisys.jar
ENV HOST_IP=127.0.0.1
ENV POSTGRES_IP=127.0.0.1
CMD java -jar klinisys.jar