PROJECT_NAME=spring-gift-point

cd $PROJECT_NAME

echo "> Git Pull"

git pull

echo "> 프로젝트 build 시작"

./gradlew bootJar

cd ../

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo ">kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo ">새 어플리케이션 배포"

cd $PROJECT_NAME/build/libs/

JAR_NAME=$(ls -tr | grep '.*[.]jar' | grep -v plain | tail -n 1)

echo ">JAR Name : $JAR_NAME"

nohup java -jar $JAR_NAME 2>&1 &