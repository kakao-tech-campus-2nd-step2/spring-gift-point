#!/bin/bash

echo "> git pull..."
git pull

echo "> build..."
./gradlew bootJar

CURRENT_PID=$(pgrep -f *.jar)
if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

cd build/libs/
echo "> deploy..."
JAR=$(ls -tr *.jar | tail -n 1)
nohup java -jar $JAR 2>&1 &