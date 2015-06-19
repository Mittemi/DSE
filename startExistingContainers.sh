docker start mongo
docker start postgres
echo "Wait a few seconds for docker and postgres"
sleep 5
docker start klinisys
echo "Wait a few seconds for klinisys to start"
sleep 20
docker start apigateway
docker start reservation
echo "Initialize Demo Data for Klinisys"
curl http://localhost:9000/demo
echo -e "\n"
docker start opmatcher
docker start notifier

