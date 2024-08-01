#!/bin/bash

# 현재 디렉토리에서 빌드된 JAR 파일을 찾습니다.
BUILD_PATH=$(ls ../build/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

# 현재 실행 중인 프로세스를 찾습니다.
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "No running process found for $JAR_NAME"
  sleep 1
else
  echo "Killing process $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 배포 경로 설정
DEPLOY_PATH=/home/ubuntu/repository/spring-gift-point/
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "Deploying $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &
