# Use the official Eclipse Temurin base image with JDK 21
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the application JAR file to the container
COPY build/libs/scheduler-0.0.1-SNAPSHOT.jar /app/server.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/server.jar"]
