#!/bin/bash
echo "🌈 Github에서 프로젝트를 Pull 합니다."

git pull

echo "🌈 SpringBoot 프로젝트 빌드를 시작합니다."
./gradlew clean
./gradlew bootJar

BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "🌈 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
  sleep 1
else
  echo "🌈 구동중인 애플리케이션을 종료했습니다. (pid : $CURRENT_PID)"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/spring-gift-point/
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

echo "🌈 SpringBoot 애플리케이션을 실행합니다."
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &