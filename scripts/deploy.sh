#!/bin/bash
REPOSITORY=/home/ubuntu
PROJECT_NAME=spring-gift-point

echo "> 백엔드 프로젝트 디렉토리로 이동"

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull 백엔드 프로젝트"

git pull

echo "> 백엔드 프로젝트 Build 시작"

./gradlew build

echo "> 기본 디렉토리로 이동"

cd $REPOSITORY

echo "> Build 파일 복사"

BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)

cp $BUILD_PATH $REPOSITORY/

JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
  sleep 1
else
  echo "> 구동중인 애플리케이션을 종료했습니다. (pid : $CURRENT_PID)"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

cd $REPOSITORY

JAR_NAME=$(ls $REPOSITORY/ | grep 'spring-gift-point' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME &