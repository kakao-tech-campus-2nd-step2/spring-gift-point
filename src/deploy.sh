#!/bin/bash
JAR_PATH=$(ls /home/ubuntu/gift/*.jar)
DIR_PATH=$(dirname $JAR_PATH)
JAR_NAME=$(basename $JAR_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
  sleep 1
else
  echo "> 현재 구동중인 애플리케이션을 종료합니다. kill $CURRENT_PID"
  kill $CURRENT_PID
  sleep 3
fi

echo "> 디렉토리를 이동합니다."
cd $DIR_PATH

if [ -f ./nohup.out ]; then
  echo "> nohup.out 파일이 존재하므로 삭제합니다."
  rm ./nohup.out
fi

echo "> 새 애플리케이션을 배포합니다."
nohup java -jar $JAR_NAME &