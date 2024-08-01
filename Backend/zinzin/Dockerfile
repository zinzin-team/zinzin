# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Maintainer information and other metadata
LABEL maintainer="hckhong@naver.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file (assumes that the JAR file is named "app.jar")
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} zinzin.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/zinzin.jar"]
