#!/bin/bash

#GitHub 주소, 브랜치
GIT_REPOSITORY="https://github.com/KBM05haruin30/spring-gift-point.git"
GIT_BRANCH="step1"

# 프로젝트 경로 및 빌드된 JAR 파일 경로 설정
PROJECT_PATH="/home/ubuntu/repository/spring-gift-point"
BUILD_PATH="$PROJECT_PATH/build/libs/spring-gift-0.0.1-SNAPSHOT.jar"
JAR_NAME=$(basename $BUILD_PATH)

# GitHub에서 최신 코드 가져오기
if [ ! -d "$PROJECT_PATH" ]; then
  echo "깃허브 저장소 클론"
  git clone --branch $GIT_BRANCH $GIT_REPOSITORY $PROJECT_PATH
else
  cd $PROJECT_PATH
  git fetch origin
  git checkout $BRANCH
  git pull origin $BRANCH
fi

# 빌드
cd $PROJECT_PATH
./gradlew build

# 현재 실행 중인 애플리케이션 PID 확인
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 애플리케이션이 실행 중인 경우 종료
if [ -z $CURRENT_PID ]
then
  echo "실행중인 애플리케이션 없음"
  sleep 1
else
  echo "PID로 애플리케이션 죽이기: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 배포 경로로 JAR 파일 복사
DEPLOY_PATH="/home/ubuntu/"
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

# 새로운 애플리케이션 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "배포중 $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

echo "배포 완료"