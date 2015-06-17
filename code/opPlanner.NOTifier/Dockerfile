FROM dockerfile/java:oracle-java8
MAINTAINER dse@kraenkl.com
EXPOSE 8080
ADD build/libs/opPlanner.NOTifier-0.0.1-SNAPSHOT.jar /data/notifier.jar
CMD java -jar notifier.jar