#!/bin/bash
# 변수 설정
DEPLOY_PATH="/home/ubuntu/spring-gift-point"
REPO_URL="https://github.com/yjhannn/spring-gift-point.git"
JAR_LOCATION="${DEPLOY_PATH}/build/libs/*.jar"
BRANCH="main"

# 현재 실행 중인 애플리케이션 PID
CURRENT_PID=$(pgrep -f $JAR_LOCATION)

# 애플리케이션 실행 중인 경우 종료
if [ -z $CURRENT_PID]
then
  echo "현재 실행 중인 애플리케이션이 없습니다."
else
  echo "PID: $CURRENT_PID 인 애플리케이션 종료 중입니다."
  kill -15 $CURRENT_PID
  sleep 5
fi

# 깃 관련 작업
if [ ! -d ${DEPLOY_PATH}]
then
  echo "레포지토리 클론 작업 실행"
  git clone "${REPO_URL}" "${DEPLOY_PATH}"
else
  echo "변경된 사항 패치"
  cd "${DEPLOY_PATH}"
  git fetch
  git checkout ${BRANCH}
  git pull origin ${BRANCH}
fi

# 애플리케이션 실행
echo "애플리케이션을 빌드합니다."
cd "${DEPLOY_PATH}"
chmod +x ./gradlew
./gradlew bootJar

echo "애플리케이션을 시작합니다."
cd build/libs/
nohup java -jar "${JAR_LOCATION}" &



