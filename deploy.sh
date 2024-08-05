#!/bin/bash


REPOSITORY=/home/ubuntu/repo/stepone
PROJECT_NAME=spring-gift-point


cd $REPOSITORY/$PROJECT_NAME/


echo "> Git Pull"
git pull origin step1


echo "> project build start"
./gradlew build -x test

echo "> directory로 이동"
cd $REPOSITORY


echo "> build 파일 복사"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/


PORT=8080
echo "> 포트 $PORT 을 사용하는 프로세스 확인"
PORT_PID=$(lsof -ti tcp:$PORT)

if [ -z "$PORT_PID" ]; then
    echo "> 포트 $PORT 을 사용하는 프로세스가 없습니다."
else
    echo "> 포트 $PORT 을 사용하는 프로세스 종료: $PORT_PID"
    kill -9 $PORT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"
nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &