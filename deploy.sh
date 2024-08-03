#!/bin/bash

# /home/ubuntu/spring-gift-point/build/libs/*.jar 에있는 jar 파일 변수설정
BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH) # jar 파일 명 추출 ex) spring-gift-0.0.1-SNAPSHOT.jar

CURRENT_PID=$(pgrep -f $JAR_NAME) # JAR_NAME과 일치하는 프로세스 ID를 찾아 저장

if [ -z $CURRENT_PID ] # 비어있으면 1초 대기 후
then
  echo "빌드 시작" # 터미널에 빌드 시작 출력top
  sleep 1
else
  kill -15 $CURRENT_PID # 비어있지 않으면 kill -15 명령어로 프로세스 종료 후 5초 대기
  sleep 5 # 프로그램이 종료 될때 까지 대기
fi

DEPLOY_PATH=/home/ubuntu/spring-gift-point/ # 쉘 스크립트 경로
cp $BUILD_PATH $DEPLOY_PATH # jar파일을 $DEPLOY_PATH로 복사
cd $DEPLOY_PATH # DEPLOY_PATH로 이동

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME # 이동된 jar파일

# nohup 명령어로 터미널 세션이 종료돼도 계속 실행
# nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &
java -jar $DEPLOY_JAR



