#!/bin/bash

# JAR 파일 이름 설정
JAR_NAME=spring-gift-1.0-SNAPSHOT.jar

# 현재 실행 중인 애플리케이션의 PID를 가져옴
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 실행 중인 애플리케이션이 있으면 종료
if [ -z $CURRENT_PID ]
then
  echo "No application is currently running."
else
  echo "Stopping current application with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5

  # 프로세스가 아직 종료되지 않았는지 확인
  CURRENT_PID=$(pgrep -f $JAR_NAME)
  if [ -z $CURRENT_PID ]
  then
    echo "Application stopped successfully."
  else
    echo "Application did not stop, killing it forcefully with PID: $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 2
  fi
fi

# git pull을 통해 변경사항 가져오기
echo "Pulling latest changes from git..."
cd /home/ubuntu/spring-gift-point
git pull

# 기존 빌드 폴더 삭제
echo "Removing old build directory..."
rm -rf build

# 새로운 빌드 수행
echo "Building the application..."
./gradlew build

# 빌드된 JAR 파일 경로 설정
BUILD_PATH=$(ls build/libs/*SNAPSHOT.jar | grep -v 'plain')
JAR_NAME=$(basename $BUILD_PATH)

# 새로운 애플리케이션 실행
echo "Starting new application..."
cd build/libs
nohup java -jar $JAR_NAME > output.log 2>&1 &

echo "Deployment completed."
