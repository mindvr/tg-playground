# syntax=docker/dockerfile:1

FROM amazoncorretto:17 AS build

# Set the working directory
WORKDIR /app

# Copy the gradle files to the Docker image
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the application code
COPY src src

# Build the application
RUN ./gradlew bootJar --no-daemon

# Use OpenJDK for runtime
FROM amazoncorretto:17-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage to the runtime stage
COPY --from=build /app/build/libs/*.jar app.jar

# Set the startup command to execute your binary
ENTRYPOINT ["java","-Dtg.token=${TG_TOKEN}","-Dtg.name=${TG_NAME}", "-jar","/app/app.jar"]
