#!/bin/bash

# JAR 파일 이름 설정
JAR_NAME=spring-gift-1.0-SNAPSHOT.jar

# 현재 실행 중인 애플리케이션의 PID를 가져온다.
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 1. 실행 중인 애플리케이션이 있으면 종료시킨다.
if [ -z $CURRENT_PID ]
then
  echo "[1] 현재 배포된 서버가 없습니다."
  echo "[2] 종료시킬 애플리케이션이 없습니다."
else
  echo "[1] 현재 배포된 서버를 중단시킵니다. 현재 배포된 서버의 PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5

  # 2. 프로세스가 아직 종료되지 않았는지 확인한다.
  CURRENT_PID=$(pgrep -f $JAR_NAME)
  if [ -z $CURRENT_PID ]
  then
    echo "[2] 애플리케이션이 성공적으로 종료되었습니다."
  else
    echo "[2] 애플리케이션이 아직 종료되지 않았기에 강제 종료합니다. 현재 배포된 서버의 PID : $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 2
  fi
fi

# 3. git pull을 통해 변경사항 가져오기
echo "[3] git pull을 통해 소스코드를 최신 상태로 업데이트합니다."
cd /home/ubuntu/spring-gift-point
git pull

# 4. 기존 빌드 폴더 삭제
echo "[4] 기존 빌드 폴더를 삭제합니다."
rm -rf build

# 5. 새로운 빌드 수행
echo "[5] 새로운 빌드를 시작합니다."
./gradlew build

# 빌드된 JAR 파일 경로 설정
BUILD_PATH=$(ls build/libs/*SNAPSHOT.jar | grep -v 'plain')
JAR_NAME=$(basename $BUILD_PATH)

# 6. 새로운 애플리케이션 실행
echo "[6] 배포 시작!! 새로운 애플리케이션을 실행합니다. JAR 파일명: $JAR_NAME"
cd build/libs
nohup java -jar $JAR_NAME > output.log 2>&1 &

echo "[7] 배포가 완료되었습니다."
