#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/build/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
CURRENT_PID=$(pgrep -f $JAR_NAME)

#실행 중인 프로세스 확인
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

#배포
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &
