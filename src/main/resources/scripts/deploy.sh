#!/bin/bash

# 설정 부분
REPO_URL="https://github.com/rbm0524/spring-gift-point.git"
REPO_DIR="/home/ubuntu/spring-gift-point"
BUILD_DIR="$REPO_DIR/build/libs"
DEPLOY_PATH="/home/ubuntu/"
LOG_FILE="/home/ubuntu/deploy.log"
BRANCH="step3"

# 레포지토리 클론 또는 업데이트
if [ ! -d "$REPO_DIR" ]; then
  printf "레포지토리 클론 중...\n"
  git clone $REPO_URL $REPO_DIR
fi

printf "레포지토리 업데이트 및 브랜치 체크아웃 중...\n"
cd $REPO_DIR
git fetch origin
git checkout $BRANCH
git pull origin $BRANCH

printf "프로젝트 빌드 중...\n"
./gradlew build

printf "빌드 경로에서 JAR 파일 찾는 중...\n"
BUILD_PATH=$(ls $BUILD_DIR/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
printf "빌드된 JAR 파일: %s\n" $JAR_NAME

printf "현재 실행 중인 애플리케이션 PID 확인 중...\n"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  printf "현재 실행 중인 애플리케이션이 없습니다.\n"
else
  printf "실행 중인 애플리케이션(PID: %s) 종료 중...\n" $CURRENT_PID
  kill -15 $CURRENT_PID
  sleep 5
fi

printf "JAR 파일을 배포 경로로 복사 중...\n"
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
printf "애플리케이션을 백그라운드에서 시작 중...\n"
nohup java -jar $DEPLOY_JAR > $LOG_FILE 2>&1 &

printf "애플리케이션 배포 완료. 로그 파일: %s\n" $LOG_FILE

# tail -f $LOG_FILE 명령어로 배포 로그를 확인할 수 있습니다.
