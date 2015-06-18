FROM dockerfile/java:oracle-java8
MAINTAINER dse@kraenkl.com
EXPOSE 9001
ADD build/libs/opPlanner.OPmatcher-0.0.1-SNAPSHOT.jar /data/opmatcher.jar
CMD java -jar opmatcher.jar