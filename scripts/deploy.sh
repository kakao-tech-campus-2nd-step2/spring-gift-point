#!/bin/bash

# 프로젝트 루트 디렉토리 설정
PROJECT_ROOT=$(pwd)

# JAR 파일 찾기
BUILD_PATH=$(find $PROJECT_ROOT -name '*.jar' | grep '/build/libs/')
if [ -z "$BUILD_PATH" ]; then
    echo "Error: JAR file not found"
    exit 1
fi
JAR_NAME=$(basename $BUILD_PATH)

# 현재 실행 중인 프로세스 확인
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 실행 중인 프로세스가 있으면 종료
if [ -z "$CURRENT_PID" ]
then
  echo "No process running."
else
  echo "Killing process $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 배포 경로 설정
DEPLOY_PATH=$PROJECT_ROOT/

# JAR 파일 복사
cp $BUILD_PATH $DEPLOY_PATH

# 배포 디렉토리로 이동
cd $DEPLOY_PATH

# JAR 파일 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "Deploying $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

echo "Deployment completed"