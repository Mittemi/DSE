#!/bin/bash

## Remove mongo db if exists ##
echo "remove previous mongo container if exists..."
docker rm -f mongo

## Start mongo db ##
docker run --name mongo -d -v /mongo -p 27017:27017 -p 28017:28017 -e AUTH=no tutum/mongodb

## Remove postgres db if exists ##
echo "remove previous postgres container if exists..."
docker rm -f postgres

## Start postgres db ##
docker run --name postgres -v /psql -p 5432:5432 -d -e "POSTGRES_PASSWORD=passme" postgres 
docker exec -it postgres psql -h localhost -U postgres --command "CREATE DATABASE klinisys"

