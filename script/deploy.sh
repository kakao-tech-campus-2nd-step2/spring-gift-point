echo "배포 시작..\n"

# 1. 변수 정의 (디렉토리 경로)
PROJECT_NAME="spring-gift-point"
PROJECT_JAR_FILE="spring-gift-point-0.0.1-SNAPSHOT"
GIT_REPOSITORY="https://github.com/jjt4515/spring-gift-point.git"
GIT_BRANCH="step1"
PORT=8080

echo "PID: $(lsof -i :${PORT} -t)"

# 2. if 분기로 포트가 켜져있는지 확인 (서버 on 여부) -> 변수로 정의
# 만약 서버가 on이라면 자동으로 종료를 시킨다.
if [ -n $(lsof -i :${PORT} -t) ]; then
        echo "켜져있는 서버 종료중.."
        echo "$(sudo kill -9 $(lsof -i :${PORT} -t))"
fi


# 4. 깃pull로 최신 업데이트
echo "프로젝트 최신화.."
cd ~/${PROJECT_NAME} && git pull origin ${GIT_BRANCH}


# 5. 빌드 시작
echo "빌드 시작.."
cd ~/${PROJECT_NAME} && ./gradlew build


# 6. /build/libs 들어가서 jar 무중단 실행
echo "서버 실행.."
cd ~/${PROJECT_NAME}/build/libs && nohup java -jar ${PROJECT_JAR_FILE}.jar &