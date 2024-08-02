#!/bin/bash

# 환경 변수 설정
DEPLOY_DIR="/home/ubuntu/spring-gift-point"
BRANCH="step1"

BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

##최신 코드 반영
echo "spring-gift-point로 이동"
cd $DEPLOY_DIR

echo "Git fetch origin"
git fetch origin

echo "Git checkout BRANCH"
git checkout $BRANCH

echo "Git pull origin BRANCH"
git pull origin $BRANCH

##기존 실행중이던 process kill 후 재 빌드 후 다시 실행
echo "현재 실행 중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "현재 실행 중인 애플리케이션이 없습니다."
else
  echo "현재 실행 중인 애플리케이션 종료: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "배포 디렉토리 생성"
DEPLOY_PATH=/home/ubuntu/spring-gift-point/app/
mkdir -p $DEPLOY_PATH

echo "빌드된 파일 배포 경로로 이동"
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME

echo "애플리케이션 백그라운드에서 시작"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

echo "배포 완료!"