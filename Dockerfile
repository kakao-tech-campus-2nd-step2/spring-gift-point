FROM openjdk:21

ADD /build/libs/*.jar spring-gift.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "spring-gift.jar", "--spring.profiles.active=dev"]
