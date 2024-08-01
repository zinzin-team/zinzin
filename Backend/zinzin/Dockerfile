# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Maintainer information and other metadata
LABEL maintainer="hckhong@naver.com"

# Set the working directory in the container
WORKDIR /app

# Copy the application's jar file to the container
COPY build/libs/zinzin-*-SNAPSHOT.jar /app/zinzin.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/zinzin.jar"]
