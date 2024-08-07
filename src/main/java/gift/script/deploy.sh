#!/bin/bash

REPOSITORY=/home/ubuntu/repository
PROJECT_NAME=spring-gift-point

echo "> 프로젝트 파일로 이동"
cd $REPOSITORY/$PROJECT_NAME

echo "> Git fetch origin"
git fetch origin

echo "> Git checkout minju26"
git checkout minju26

echo "> Git pull"
git pull origin minju26

echo "> 프로젝트 Build 시작"
./gradlew build

echo "> 레포지토리 이동"
cd $REPOSITORY

echo "> Build 파일 복사"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 실행중인 어플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f "${PROJECT_NAME}.*\.jar")

echo "> 현재 실행중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 실행중인 어플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | grep -v 'plain' | tail -n 1)

echo "> JAR name: $JAR_NAME"

nohup java -jar \
  -Dspring.config.location=/home/ubuntu/app/application.properties \
  -Dspring.profiles.active=real \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
