# Use the OpenJDK 21 image
FROM openjdk:21-jdk-oraclelinux7

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the image
COPY build/libs/*.jar app.jar

# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
