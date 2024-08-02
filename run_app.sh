#!/bin/bash
PROJECT_DIR=/home/ubuntu/repository/KTC_spring-gift-point/
BUILD_DIR=$PROJECT_DIR/build/libs/
BUILD_PATH=$(ls $BUILD_DIR*.jar)
JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)
echo "현재 PID : {$CURRENT_PID}"

if [ -z $CURRENT_PID ]
then
  echo "기존에 실행 중인 애플리케이션이 없습니다."
else
  kill -9 $CURRENT_PID
  echo "기존 애플리케이션 종료: $CURRENT_PID"
  sleep 5
fi

cd $PROJECT_DIR
rm -rf build
echo "현재 빌드 삭제."

./gradlew bootJar
cd $BUILD_DIR
echo "프로젝트 빌드."

BUILD_PATH=$(ls $BUILD_DIR*.jar)
JAR_NAME=$(basename $BUILD_PATH)

nohup java -jar $JAR_NAME > /dev/null 2> /dev/null < /dev/null &
echo "프로젝트 실행."