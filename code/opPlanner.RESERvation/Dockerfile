FROM java:8
MAINTAINER dse@kraenkl.com
EXPOSE 9002
WORKDIR /data/
ADD build/libs/opPlanner.RESERvation-0.0.1-SNAPSHOT.jar /data/reservation.jar
CMD java -jar reservation.jar