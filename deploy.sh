#!/bin/sh
PROJECT_PATH=/home/ubuntu/step2
PROJECT_NAME=spring-gift-point
BUILD_PATH=build/libs
JAR_NAME=$(basename $PROJECT_PATH/$PROJECT_NAME/.*jar)

cd $PROJECT_PATH/$PROJECT_NAME

echo ">Git pull"
git pull origin step2

echo ">Build 시작"
./gradlew bootJar

echo ">디렉토리 이동"
cd $PROJECT_PATH

echo ">Build 파일 복사"
cp $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH/*.jar $PROJECT_PATH

echo ">실행중 PID 확인"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]; then
        echo " >실행중인 애플리케이션이 없어서 바로 실행됩니다..\n"
else
        echo "실행중인 애플리케이션이 있어서 이를 종료합니다. [PID = $CURRENT_PID]\n"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo ">새 애플리케이션 배포"
JAR_NAME=$(ls -tr $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH | grep *.jar | tail -n 1)
nohup java -jar $JAR_NAME &