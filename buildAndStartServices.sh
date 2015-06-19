#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo -e "#################\n
ERROR - No Parameter found\nPlease provide the IP-Address of your Docker Host"
	exit 1
fi
echo -e "\n\n###################################################"
echo -e "## DSE - Distributed Systems Engineering SS 2015 ##"
echo -e "###################################################"
cd code/
cd opPlanner.KLINIsys/
echo -e "\n\n\n##### Starting KliniSys #####\n"
sleep 5
docker exec -it postgres psql -h localhost -U postgres --command "CREATE DATABASE klinisys"
sh containerbau.sh $1
cd ..
cd opPlanner.RESERvation/
echo -e "\n\n\n##### Starting Reservation #####\n"
sh containerbau.sh $1
cd ..
cd opPlanner.ApiGateway/
echo -e "\n\n\n##### Starting API Gateway #####\n"
sh containerbau.sh $1
cd ..
cd opPlanner.NOTifier/
echo -e "\n\n\n##### Starting Notifier #####\n"
sh containerbau.sh $1
cd ..
echo -e "\n\n#### KLINISYS insert demo DATA #####\n"
curl "http://$1:9000/demo"
cd opPlanner.OPmatcher/
echo -e "\n\n\n##### Starting OP Matcher #####\n"
sh containerbau.sh $1
cd ..
cd opPlanner.WebUI
echo -e "\n\n\n##### Starting WebUI #####\n"
sh containerbau.sh $1
echo -e "\n ##### DONE - I'm going home now ! #####"
