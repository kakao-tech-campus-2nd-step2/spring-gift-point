#!/bin/bash

# JAR 파일 경로 확인
BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

# 현재 실행 중인 애플리케이션 PID 확인
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 실행 중인 애플리케이션 종료
if [ -z "$CURRENT_PID" ]; then
  echo "No application running, so no need to stop."
else
  echo "Stopping application with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 배포 디렉토리 생성
DEPLOY_PATH=/home/ubuntu/spring-gift-point/app/
mkdir -p $DEPLOY_PATH

# JAR 파일 복사 및 배포
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "Deploying $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /home/ubuntu/spring-gift-point/build/libs/nohup.out 2>&1 &

# 로그 파일 모니터링
tail -f /home/ubuntu/spring-gift-point/build/libs/nohup.out
