#!/bin/bash

echo "Preparing environment..."

docker-compose down 2>/dev/null

echo "Downloading dependencies and compiling..."
./mvnw clean install -DskipTests

echo "Starting containers with live reload..."
docker-compose up --build --watch
