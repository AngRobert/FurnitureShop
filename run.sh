#!/bin/bash

if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
fi

docker compose up -d

mvn compile
mvn exec:java -Dexec.mainClass="com.AngRobert.Zpotifai.Main"
