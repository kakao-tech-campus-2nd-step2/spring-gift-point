#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/spring-gift-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> 현재 구동 중인 애플리케이션 pid 확인"
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
DEPLOY_PATH=/home/ubuntu/spring-gift-point/
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포: $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR &