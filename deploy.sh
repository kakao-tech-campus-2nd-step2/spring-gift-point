#!/bin/bash

# 빌드된 JAR 파일의 경로를 지정
BUILD_PATH=$(ls /home/ubuntu/repository/spring-gift-point/build/libs/spring-gift-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_PATH)

# 현재 실행 중인 애플리케이션의 PID
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 애플리케이션이 실행 중인지 확인
if [ -z "$CURRENT_PID" ]; then
  echo "No application is currently running."
else
  echo "Stopping application with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# JAR 파일을 배포 경로로 복사
DEPLOY_PATH=/home/ubuntu/
echo "Copy $JAR_NAME to $DEPLOY_PATH"
cp $BUILD_PATH $DEPLOY_PATH

# 배포 경로로 이동하여 애플리케이션을 백그라운드에서 실행
cd $DEPLOY_PATH
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "Start $JAR_NAME"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

echo "Deployment complete."