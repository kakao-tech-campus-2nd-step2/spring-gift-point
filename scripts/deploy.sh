#!/bin/sh
PROJECT_PATH=/home/ubuntu
PROJECT_NAME=spring-gift-point
BUILD_PATH=build/libs
JAR_NAME=$(basename $PROJECT_PATH/$PROJECT_NAME/.*jar)

echo "> 프로젝트로 이동"
cd $PROJECT_PATH/$PROJECT_NAME

echo "> Git pull origin step3"
git pull origin step3

echo "> 프로젝트 빌드 위치로 이동"
cd $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH

echo "> 프로젝트 Build 시작"
./gradlew bootJar

echo "> 프로젝트 디렉토리로 이동"
cd $PROJECT_PATH

echo "> Build 파일 복사"
cp $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH/*.jar $PROJECT_PATH

echo "> 현재 실행중인 PID 확인"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]; then
  echo "  ㄴ>실행중인 애플리케이션이 없어서 바로 실행됩니다."
else
  echo "  ㄴ> 실행중인 애플리케이션이 있어서 이를 종료합니다. [PID = $CURRENT_PID]"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH | grep *.jar | tail -n 1)
java -jar $JAR_NAME &
nohup java -jar $JAR_NAME &