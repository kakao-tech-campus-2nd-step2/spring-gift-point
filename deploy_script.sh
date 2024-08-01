#!/bin/bash

# 설정 변수
BUILD_DIR="/home/ubuntu/spring-gift-point" # 프로젝트의 backend 폴더
BUILD_OUTPUT_DIR="$BUILD_DIR/build/libs" # 빌드 출력 디렉토리
DEPLOY_DIR="/home/ubuntu"
JAR_NAME="app.jar" # 빌드된 JAR 파일명
BUILD_CMD="./gradlew clean build" # 빌드 명령어

# 현재 실행 중인 프로세스 ID 확인
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 실행 중인 프로세스가 있는 경우 종료
if [ -n "$CURRENT_PID" ]; then
  echo "> 현재 실행 중인 프로세스 종료: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
else
  echo "> 실행 중인 프로세스 없음"
fi

# 빌드 수행
echo "> 빌드 시작"
$BUILD_CMD -p $BUILD_DIR

# 빌드 결과 확인
BUILD_PATH="$BUILD_OUTPUT_DIR/$JAR_NAME"
if [ ! -f "$BUILD_PATH" ]; then
  echo "> 빌드 실패: $JAR_NAME 파일을 찾을 수 없습니다."
  exit 1
fi

# 빌드된 파일 배포
echo "> 빌드된 파일 배포: $JAR_NAME"
cp $BUILD_PATH $DEPLOY_DIR

# 배포된 JAR 파일 실행
DEPLOY_JAR="$DEPLOY_DIR/$JAR_NAME"
echo "> 배포된 JAR 파일 실행: $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

echo "> 배포 완료"
