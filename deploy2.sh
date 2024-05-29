#!/bin/bash

# Copy the data file to the application's resources directory
cp patttyparadise.json ./src/main/resources/meals.json

# Step 1: Build the project with Maven
mvn clean package

# Step 2: Create a new builder instance and switch to it
docker buildx create --use

# Step 3: Build the Docker image
docker buildx build --platform linux/amd64 -t tristanpelgrims/supplier2 --load .

# Step 4: Push the Docker image to the registry
docker push tristanpelgrims/supplier2

# Step 5: SSH into the remote server, stop the existing container, pull the Docker image and run a new container
SSHPASS='azureadmin2024!' sshpass -e ssh azureuser@20.166.252.28 << EOF
docker stop \$(docker ps -q --filter ancestor=tristanpelgrims/supplier2)
docker pull tristanpelgrims/supplier2
docker run -d -p 8080:8080 tristanpelgrims/supplier2
EOF