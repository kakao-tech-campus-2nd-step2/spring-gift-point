#!/bin/bash

REPO_URL="https://github.com/cussle/spring-gift-point.git"
BASE_DIR="/home/ubuntu/repository"
PROJECT_DIR="$BASE_DIR/spring-gift-point"
BUILD_DIR="$PROJECT_DIR/build/libs"
JAR_NAME="spring-gift-0.0.1-SNAPSHOT.jar"
PORT=8080

# 1. 프로젝트 클론 또는 업데이트
cd $BASE_DIR

if [ ! -d "$PROJECT_DIR" ]; then
  git clone $REPO_URL
else
  cd $PROJECT_DIR
  git pull
fi

# 2. 기존 프로세스 종료
CURRENT_PID=$(lsof -ti tcp:$PORT)

if [ -z "$CURRENT_PID" ]; then
  echo "No application is running on port $PORT"
else
  echo "Stopping application running on port $PORT with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 3. 프로젝트 빌드
cd $PROJECT_DIR
./gradlew bootJar

# 4. 새로운 JAR 파일 실행
cd $BUILD_DIR
nohup java -jar $JAR_NAME > /dev/null 2> /dev/null < /dev/null &
