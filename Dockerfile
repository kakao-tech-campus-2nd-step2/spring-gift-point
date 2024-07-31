FROM openjdk:21
ADD /build/libs/*.jar spring-gift.jar
ENTRYPOINT ["java", "-jar", "spring-gift.jar"]