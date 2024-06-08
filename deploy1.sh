#!/bin/bash

# Copy the data file to the application's resources directory
cp pastabella.json ./src/main/resources/meals.json

# Step 1: Build the project with Maven
mvn clean package

# Step 2: Create a new builder instance and switch to it
docker buildx create --use

# Step 3: Build the Docker image
docker buildx build --platform linux/amd64 -t tristanpelgrims/supplier1 --load .

# Step 4: Push the Docker image to the registry
docker push tristanpelgrims/supplier1

# Step 5: SSH into the remote server, stop the existing container, pull the Docker image and run a new container
SSHPASS='azureuser2024!' sshpass -e ssh azureuser@20.234.67.158 << EOF
docker stop \$(docker ps -q --filter ancestor=tristanpelgrims/supplier1)
docker pull tristanpelgrims/supplier1
docker run -d -p 8080:8080 tristanpelgrims/supplier1
EOF