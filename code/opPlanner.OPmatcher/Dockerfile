FROM java:8
MAINTAINER dse@kraenkl.com
EXPOSE 9001
WORKDIR /data/
ADD build/libs/opPlanner.OPmatcher-0.0.1-SNAPSHOT.jar /data/opmatcher.jar
CMD java -jar opmatcher.jar