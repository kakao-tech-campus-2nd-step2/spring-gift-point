#!/bin/bash
REPOSITORY=/home/ubuntu/spring-gift-point
DEPLOY_PATH=/home/ubuntu/

echo "> 해당 repository로 이동"
cd "$REPOSITORY" || exit

echo "> git pull origin step2"
git pull origin step2

echo "> 프로젝트 build 시작"
./gradlew build

echo "build 파일 복사"
BUILD_PATH=$(ls ./build/libs/*.jar)
JAR_NAME=$(basename "$BUILD_PATH")
cp "$BUILD_PATH" "$DEPLOY_PATH"

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f "$JAR_NAME")

if [ -n "$CURRENT_PID" ]; then
  echo "> 현재 애플리케이션 종료"
  kill -15 "$CURRENT_PID"
  sleep 5
else
  echo "> 현재 구동중인 애플리케이션 없음"
fi

echo "> api 실행"
DEPLOY_JAR="$DEPLOY_PATH$JAR_NAME"
nohup java -jar "$DEPLOY_JAR" > "$DEPLOY_PATH/app.log" 2>&1 &
