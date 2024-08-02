#!/bin/bash

printf "빌드 경로에서 JAR 파일 찾는 중...\n"
BUILD_PATH=$(ls /home/ubuntu/repository/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
printf "빌드된 JAR 파일: %s\n" $JAR_NAME

printf "현재 실행 중인 애플리케이션 PID 확인 중...\n"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  printf "현재 실행 중인 애플리케이션이 없습니다.\n"
  sleep 1
else
  printf "실행 중인 애플리케이션(PID: %s) 종료 중...\n" $CURRENT_PID
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/
printf "JAR 파일을 배포 경로로 복사 중...\n"
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
printf "애플리케이션을 백그라운드에서 시작 중...\n"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

printf "애플리케이션 배포 완료. 로그 파일: %s\n" $LOG_FILE

# tail -f /home/ubuntu/deploy.log
# 위 명령어로 배포 로그를 확인