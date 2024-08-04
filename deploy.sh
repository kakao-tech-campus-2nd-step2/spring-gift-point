#!/bin/bash
REPOSITORY=/home/ubuntu/spring-gift-point
PROJECT_NAME=spring-gift-point

cd $REPOSITORY

echo "> Git Pull"
git pull

echo "> 프로젝트 Build 시작"
./gradlew build

echo "> Build 파일 복사"
cp $REPOSITORY/build/libs/spring-gift-0.0.1-SNAPSHOT.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(lsof -i tcp:8080 | awk 'NR!=1 {print$2}')

echo "현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -9 $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$REPOSITORY/build/libs/spring-gift-0.0.1-SNAPSHOT.jar

echo "> JAR Name: $JAR_NAME"

nohup java -jar \
    -Dspring.config.location=file:/home/ubuntu/spring-gift-point/src/main/resources/application.properties \
    $JAR_NAME 2>&1 &
