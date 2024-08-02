#!/bin/bash

# 환경 변수 설정
APP_NAME="spring-gift-point"
REPO_URL="https://github.com/humpose/spring-gift-point.git"
DEPLOY_DIR="/home/ubuntu/${APP_NAME}"
JAR_FILE="${DEPLOY_DIR}/build/libs/spring-gift-0.0.1-SNAPSHOT.jar"
BACKUP_DIR="/home/ubuntu/backup"
BRANCH="step3" # 배포할 브랜치

# 현재 디렉토리 저장
CURRENT_DIR=$(pwd)

# 함수: 애플리케이션 중지
stop_application() {
    echo "Stopping existing application..."
    if pgrep -f "java -jar ${JAR_FILE}" > /dev/null; then
        pkill -f "java -jar ${JAR_FILE}"
        echo "Application stopped."
    else
        echo "No running application found."
    fi
}

# 함수: 애플리케이션 시작
start_application() {
    echo "Starting the application..."
    nohup java -jar "${JAR_FILE}" --spring.config.location=file:${DEPLOY_DIR}/application.properties > /dev/null 2>&1 &
    echo "Application started."
}

# 함수: 백업
backup_current_version() {
    if [ ! -d "${BACKUP_DIR}" ]; then
        mkdir -p "${BACKUP_DIR}"
    fi

    if [ -f "${JAR_FILE}" ]; then
        echo "Backing up current JAR file..."
        cp "${JAR_FILE}" "${BACKUP_DIR}/spring-gift-$(date +%Y%m%d%H%M%S).jar"
        echo "Backup complete."
    fi
}

# 함수: 롤백
rollback() {
    stop_application

    echo "Finding latest backup..."
    LATEST_BACKUP=$(ls -t ${BACKUP_DIR}/*.jar | head -n 1)

    if [ -z "${LATEST_BACKUP}" ]; then
        echo "No backup found. Rollback failed."
        exit 1
    fi

    echo "Rolling back to latest backup: ${LATEST_BACKUP}"
    cp "${LATEST_BACKUP}" "${JAR_FILE}"

    start_application
    echo "Rollback complete!"
}

# 롤백 모드인지 확인
if [ "$1" == "rollback" ]; then
    rollback
    exit 0
fi

# 환경 변수 로드 (민감 정보 포함된 .env 파일)
if [ -f "${DEPLOY_DIR}/.env" ]; then
    export $(cat "${DEPLOY_DIR}/.env" | xargs)
fi

# 애플리케이션 중지
stop_application

# 현재 버전 백업
backup_current_version

# 최신 코드 가져오기 및 빌드
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

# 애플리케이션 빌드
echo "Building the application..."
cd "${DEPLOY_DIR}"
chmod +x ./gradlew
./gradlew bootJar

# application.properties 복사
echo "Copying application.properties..."
cp "${CURRENT_DIR}/src/main/resources/application.properties" "${DEPLOY_DIR}"

# 애플리케이션 시작
start_application

echo "Deployment complete!"

# 원래 디렉토리로 돌아가기
cd "${CURRENT_DIR}"