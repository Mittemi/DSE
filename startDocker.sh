#!/bin/bash
echo -e "\n\n###################################################"
echo -e "## DSE - Distributed Systems Engineering SS 2015 ##"
echo -e "###################################################"
cd ./code/
cd ./opPlanner.ApiGateway/
echo -e "\n\n\n##### Starting API Gateway #####\n"
sh ./containerbau.sh
cd ..
cd ./opPlanner.KLINIsys/
echo -e "\n\n\n##### Starting KliniSys #####\n"
sh ./containerbau.sh
cd ..
cd ./opPlanner.NOTifier/
echo -e "\n\n\n##### Starting Notifier #####\n"
sh ./containerbau.sh
cd ..
cd ./opPlanner.OPmatcher/
echo -e "\n\n\n##### Starting OP Matcher #####\n"
sh ./containerbau.sh
cd ..
cd ./opPlanner.RESERvation/
echo -e "\n\n\n##### Starting Reservation #####\n"
sh ./containerbau.sh
echo -e "\n ##### DONE - I'm going home now ! #####"