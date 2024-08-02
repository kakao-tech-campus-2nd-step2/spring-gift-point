#!/bin/bash

# 프로젝트 파일 디렉토리 설정
REPO_PATH="/home/ubuntu/spring-gift-point"
cd $REPO_PATH

while true # 무한 루프
do
  # git pull로 최신 변경 사항 가져오기
  git pull

  # /home/ubuntu/spring-gift-point/build/libs/*.jar 에있는 jar 파일 변수설정
  BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)
  JAR_NAME=$(basename $BUILD_PATH) # jar 파일 명 추출 ex)

  CURRENT_PID=$(pgrep -f $JAR_NAME)

  if [ -z $CURRENT_PID ]
  then
    sleep 1
  else
    kill -15 $CURRENT_PID
    sleep 5
  fi

  DEPLOY_PATH=/home/ubuntu/spring-gift-point/
  cp $BUILD_PATH $DEPLOY_PATH
  cd $DEPLOY_PATH

  DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
  nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

  # 60초마다 업데이트 확인
  sleep 60
done
