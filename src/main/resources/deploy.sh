# 자주 사용하는 값 변수에 저장
PROJECT_NAME=spring-gift-point

# git clone 받은 위치로 이동
cd $PROJECT_NAME/build/libs/
echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID= $(pgrep -f spring-gift-0.0.1-SNAPSHOT.jar)

echo "> 현재 구동중인 애플리케이션 pid: $CURRENT_PID"
if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -9 $CURRENT_PID"
        kill -9 $CURRENT_PID
        sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=spring-gift-0.0.1-SNAPSHOT.jar

echo "> Jar Name: $JAR_NAME"
nohup java -jar $JAR_NAME 2>&1 &