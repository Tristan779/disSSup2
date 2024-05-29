# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file produced by the build into the container
COPY target/food-rest-service-0.0.1-SNAPSHOT.jar app.jar


# Run the JAR file
ENTRYPOINT ["java","-jar","app.jar"]

