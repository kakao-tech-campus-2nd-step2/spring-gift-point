#!/bin/sh

PROJECT_PATH=/home/ubuntu/repository
PROJECT_NAME=spring-gift-point
PORT=8080

echo "\n 🐳   [$PROJECT_PATH/$PROJECT_NAME] 경로로 이동합니다.\n"
cd $PROJECT_PATH/$PROJECT_NAME

echo " 🐳   최신 코드를 PULL 합니다!\n"
git pull

./gradlew bootJar

BUILD_PATH=build/libs
cd $BUILD_PATH

PID=$(lsof -i :$PORT -t)

if [ -z $PID ]; then
  echo " 🎉   실행중인 애플리케이션이 없어서 곧바로 실행합니다.\n"

else
  echo " ❌   실행중인 애플리케이션이 있어서 이를 종료합니다. [PID = $PID]\n"
  kill -15 $PID
fi

DEPLOY_JAR=$PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH/spring-gift-0.0.1-SNAPSHOT.jar
echo " 🎉   애플리케이션 실행합니다~ 🎉\n"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &
