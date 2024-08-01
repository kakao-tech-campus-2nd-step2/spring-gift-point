#!/bin/bash
REPOSITORY=/home/ubuntu/spring-gift-point

ehco "> 해당 repository로 이동"
cd $REPOSITORY

echo "> git pull origin step2"
git pull origin step2

echo "> 프로젝트 build 시작"
./gradlew build

echo "build 파일 복사"
cp ./build/libs/*.jar $REPOSITORY

echo "> 현재 구동중인 애플리케이션 pid 확인"
BUILD_PATH=$(ls $REPOSITORY/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  sleep 1
else
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &
