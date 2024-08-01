#!/bin/bash

# 환경 변수 설정
APP_NAME="spring-gift-point"
REPO_URL="https://github.com/humpose/spring-gift-point.git"
DEPLOY_DIR="/home/ubuntu/${APP_NAME}"
JAR_FILE="${DEPLOY_DIR}/build/libs/spring-gift-0.0.1-SNAPSHOT.jar"
BRANCH="step1" # 배포할 브랜치

# 현재 디렉토리 저장
CURRENT_DIR=$(pwd)

echo "Stopping existing application..."
if pgrep -f "java -jar ${JAR_FILE}" > /dev/null; then
    pkill -f "java -jar ${JAR_FILE}"
    echo "Application stopped."
else
    echo "No running application found."
fi

if [ ! -d "${DEPLOY_DIR}" ]; then
    echo "Cloning repository..."
    git clone "${REPO_URL}" "${DEPLOY_DIR}"
else
    echo "Fetching latest changes..."
    cd "${DEPLOY_DIR}"
    git fetch
    git checkout ${BRANCH}
    git pull origin ${BRANCH}
fi

echo "Building the application..."
cd "${DEPLOY_DIR}"
chmod +x ./gradlew
./gradlew bootJar

echo "Starting the application..."
cd build/libs
nohup java -jar "${JAR_FILE}" &

echo "Deployment complete!"

# 원래 디렉토리로 돌아가기
cd "${CURRENT_DIR}"