#!/bin/bash

# Load environment variables from .env if it exists
if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
fi

# Ensure the database is running
docker compose up -d

# Run the application
mvn compile
mvn exec:java -Dexec.mainClass="com.AngRobert.ShopApplication.Main"
