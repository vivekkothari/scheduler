# Build stage
FROM gradle:8.13.0-jdk23-corretto-al2023 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon -x test

# Final stage
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/scheduler-*SNAPSHOT.jar /app/server.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/server.jar"]
