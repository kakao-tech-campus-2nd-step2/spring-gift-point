#!/bin/bash

# 프로젝트 디렉토리로 이동
cd /home/ubuntu/repository/ver1/spring-gift-point

# 프로젝트 빌드
./gradlew clean bootJar

# 빌드 파일 경로 및 이름 설정
BUILD_PATH=$(ls -t build/libs/spring-gift-0.0.1-SNAPSHOT.jar | head -n 1)
if [ -z "$BUILD_PATH" ]; then
  echo "> No build file found. Exiting."
  exit 1
fi
JAR_NAME=$(basename $BUILD_PATH)

# 현재 실행 중인 애플리케이션 PID 조회
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 실행 중인 애플리케이션 종료
if [ -z "$CURRENT_PID" ]; then
  echo "> No application is currently running."
else
  echo "> Killing the current application process with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 새로운 빌드 파일 배포
DEPLOY_PATH=/home/ubuntu/
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH/$JAR_NAME
echo "> Deploying $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > $DEPLOY_PATH/nohup.out 2>&1 &
echo "> Application deployed successfully."
