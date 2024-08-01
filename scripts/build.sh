BUILD_PATH=$(ls ../build/libs/*.jar)
JAR_NAME=$(basename "$BUILD_PATH")
echo "> build 파일명: $JAR_NAME"
CURRENT_PID=$(pgrep -f "$JAR_NAME")
echo "> 현재 실행중인 애플리케이션 pid: $CURRENT_PID"
if [ -z "$CURRENT_PID" ]
then
  sleep 1
else
  echo "> 이미 실행 중인 애플리케이션이 존재하여 종료합니다.."
  kill -9 "$CURRENT_PID"
  sleep 5
fi
echo "> 프로젝트 변경 사항 로드 중.."
git pull origin gyuminbae
echo "> 프로젝트 빌드 중.."
./gradlew boodJar

DEPLOY_JAR=$BUILD_PATH$JAR_NAME
echo "> 애플리케이션을 시작합니다."
nohup java -jar "$DEPLOY_JAR" &
tail -f nohup.out