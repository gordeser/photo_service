# Dockerfile to build photo_service application

# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Copy gradle configuration files
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle

# Copy source code and configuration files
COPY src /app/src
COPY config /app/config

# Copy the gradle wrapper script
COPY gradlew /app/

# Ensure Gradle Wrapper is executable
RUN chmod +x ./gradlew

# Run Gradle build
RUN ./gradlew build --parallel --no-daemon


# Stage 2: Create the final runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/build/libs/*.jar /app/app.jar
COPY src /app/src

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

