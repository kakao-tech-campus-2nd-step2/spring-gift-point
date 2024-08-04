FROM openjdk:21-jdk-oraclelinux7

WORKDIR /app

COPY ./spring-gift-0.0.1-SNAPSHOT.jar /app/spring-gift.jar

ENV TZ=Asia/Seoul

CMD ["java", "-jar", "spring-gift.jar"]
