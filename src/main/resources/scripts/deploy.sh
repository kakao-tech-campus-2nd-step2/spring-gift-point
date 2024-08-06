#!/bin/bash

REPO_URL="https://github.com/rbm0524/spring-gift-point.git"
BRANCH="step3"
LOCAL_REPO_PATH="/home/ubuntu/repository/spring-gift-point"

printf "GitHub에서 최신 코드를 클론 또는 풀 중...\n"
if [ -d "$LOCAL_REPO_PATH" ]; then
  cd $LOCAL_REPO_PATH
  git pull origin $BRANCH
else
  git clone -b $BRANCH $REPO_URL $LOCAL_REPO_PATH
fi

printf "빌드 경로에서 JAR 파일 찾는 중...\n"
BUILD_PATH=$(ls $LOCAL_REPO_PATH/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
printf "빌드된 JAR 파일: %s\n" $JAR_NAME

printf "현재 실행 중인 애플리케이션 PID 확인 중...\n"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]; then
  printf "현재 실행 중인 애플리케이션이 없습니다.\n"
else
  printf "실행 중인 애플리케이션(PID: %s) 종료 중...\n" $CURRENT_PID
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH="/home/ubuntu/"
printf "JAR 파일을 배포 경로로 복사 중...\n"
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR="$DEPLOY_PATH$JAR_NAME"
printf "애플리케이션을 백그라운드에서 시작 중...\n"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

printf "애플리케이션 배포 완료.\n"
# 로그 파일 확인 방법을 안내
printf "로그 파일을 확인하려면 아래 명령어를 실행하세요:\n"
printf "tail -f /home/ubuntu/deploy.log\n"
