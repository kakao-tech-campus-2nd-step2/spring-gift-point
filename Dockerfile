# Start with a base image containing Java runtime
FROM openjdk:21-jre-slim

# Add Maintainer Info
LABEL maintainer="ez23rel@google.com"  # image(jdk?) manager email

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]