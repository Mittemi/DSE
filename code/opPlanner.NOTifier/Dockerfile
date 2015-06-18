FROM java:8
MAINTAINER dse@kraenkl.com
EXPOSE 9003
WORKDIR /data/
ADD build/libs/opPlanner.NOTifier-0.0.1-SNAPSHOT.jar /data/notifier.jar
CMD java -jar notifier.jar