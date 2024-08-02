#!/bin/bash
# 빌드된 JAR 파일 경로
BUILD_PATH=$(ls /home/ubuntu/repository/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename "$BUILD_PATH")

# 현재 실행 중인 프로세스 ID 확인
CURRENT_PID=$(pgrep -f "$JAR_NAME")

# 현재 프로세스가 존재하는 경우 종료
if [ -n "$CURRENT_PID" ]; then
  echo "Stopping current application with PID: $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
else
  echo "No application is currently running."
fi

# JAR 파일을 배포 디렉토리로 복사
DEPLOY_PATH=/home/ubuntu/repository/spring-gift-point/build/libs/
cp "$BUILD_PATH" "$DEPLOY_PATH"
cd "$DEPLOY_PATH" || exit

# 새로운 JAR 파일 실행
DEPLOY_JAR="$DEPLOY_PATH$JAR_NAME"
echo "Starting new application: $DEPLOY_JAR"
nohup java -jar "$DEPLOY_JAR" > /dev/null 2> /dev/null < /dev/null &