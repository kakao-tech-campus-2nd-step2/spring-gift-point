#!/bin/bash

echo -e "배포 시작..\n"

# 1. 변수 정의 (디렉토리 경로)
PROJECT_NAME="spring-gift-point"
PROJECT_JAR_FILE="spring-gift-point-0.0.1-SNAPSHOT.jar"
GIT_REPOSITORY="https://github.com/jjt4515/spring-gift-point.git"
GIT_BRANCH="step1"
PORT=8080

echo "PID: $(lsof -i :${PORT} -t)"

# 2. if 분기로 포트가 켜져있는지 확인 (서버 on 여부) -> 변수로 정의
# 만약 서버가 on이라면 자동으로 종료를 시킨다.
PID=$(lsof -i :${PORT} -t)
if [ -n "$PID" ]; then
    echo "켜져있는 서버 종료중.."
    sudo kill -9 $PID
    echo "서버 종료 완료"
else
    echo "서버가 실행 중이지 않습니다."
fi

# 3. 프로젝트 디렉토리로 이동하여 최신 업데이트
echo "프로젝트 최신화.."
if [ -d ~/${PROJECT_NAME} ]; then
    cd ~/${PROJECT_NAME} && git pull origin ${GIT_BRANCH}
else
    echo "프로젝트 디렉토리를 찾을 수 없습니다: ~/${PROJECT_NAME}"
    exit 1
fi

# 4. 빌드 시작
echo "빌드 시작.."
if [ -f ~/${PROJECT_NAME}/gradlew ]; then
    cd ~/${PROJECT_NAME} && ./gradlew build
else
    echo "Gradlew 파일을 찾을 수 없습니다: ~/${PROJECT_NAME}/gradlew"
    exit 1
fi

# 5. /build/libs 들어가서 jar 무중단 실행
echo "서버 실행.."
if [ -f ~/${PROJECT_NAME}/build/libs/${PROJECT_JAR_FILE} ]; then
    cd ~/${PROJECT_NAME}/build/libs && nohup java -jar ${PROJECT_JAR_FILE} &
else
    echo "JAR 파일을 찾을 수 없습니다: ~/${PROJECT_NAME}/build/libs/${PROJECT_JAR_FILE}"
    exit 1
fi

echo "배포 완료"
