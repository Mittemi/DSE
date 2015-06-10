#!/bin/bash

## TODO
## Beispielscript fuer DB inserts

gosu postgres postgres --single <<- EOSQL
	CREATE USER klinisys WITH PASSWORD 'secret';
	CREATE DATABASE klinisys;
	GRANT ALL PRIVILEGES ON DATABASE klinisys TO klinisys;
EOSQL
