#!/bin/bash

BUILD_DIR=/home/ubuntu/repository/spring-gift-point # 프로젝트의 backend 폴더
BUILD_OUTPUT_DIR=$BUILD_DIR/build/libs # 빌드 출력 디렉토리
JAR_NAME="app.jar" # 빌드된 JAR 파일명
BUILD_CMD="./gradlew clean build" # 빌드 명령어

echo "-----------------"
echo "현재 실행 중인 애플리케이션 종료"
echo "-----------------"

# 현재 실행 중인 프로세스 확인 및 종료
CURRENT_PID=$(pgrep -f $JAR_NAME)
if [ -z "$CURRENT_PID" ]; then
  echo "실행 중인 애플리케이션이 없습니다."
else
  echo "실행 중인 애플리케이션 종료: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "-----------------"
echo "    빌드 시작    "
echo "-----------------"
# 빌드 실행
$BUILD_CMD -p $BUILD_DIR

# 빌드 결과 확인
BUILD_PATH="$BUILD_OUTPUT_DIR/$JAR_NAME"
if [ ! -f "$BUILD_PATH" ]; then
  echo "빌드 실패: $JAR_NAME 파일을 찾을 수 없습니다."
  exit 1
fi

echo "-----------------"
echo "    배포 시작    "
echo "-----------------"
# 빌드된 JAR 파일 복사
DEPLOY_PATH=/home/ubuntu/spring-gift-point/
cp $BUILD_PATH $DEPLOY_PATH

# 새로운 JAR 파일 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "애플리케이션 시작: $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > $DEPLOY_PATH/nohup.out 2>&1 &

echo "-----------------"
echo "애플리케이션이 배포되었습니다."
echo "로그 파일: $DEPLOY_PATH/nohup.out"
echo "-----------------"
