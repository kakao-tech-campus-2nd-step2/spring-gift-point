#!/bin/bash

# 환경 설정
BUILD_DIR=/home/ubuntu/build/
DEPLOY_DIR=/home/ubuntu/
JAR_FILE=$(ls $BUILD_DIR*.jar | head -n 1)  # 최신 빌드 파일
JAR_NAME=$(basename $JAR_FILE)
DEPLOY_JAR=$DEPLOY_DIR$JAR_NAME

# 현재 실행 중인 애플리케이션의 PID 찾기
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 실행 중인 애플리케이션 종료
if [ -n "$CURRENT_PID" ]; then
  echo "현재 실행 중인 애플리케이션 종료: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 새로운 빌드 파일 배포
echo "새로운 빌드 파일 배포: $JAR_NAME"
cp $JAR_FILE $DEPLOY_DIR

# 애플리케이션 실행
echo "애플리케이션 실행: $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

echo "배포 완료!"
