#!/bin/bash

PROJECT_PATH=/home/ubuntu/spring-gift-point
PROJECT_NAME=spring-gift
BUILD_PATH=$PROJECT_PATH/build/libs

CURRENT_PID=$(pgrep -f $PROJECT_NAME)

cd $PROJECT_PATH
./gradlew clean build

if [ -z $CURRENT_PID ]
then
    echo "run!"
else
    echo "종료 후 run!"
    kill -15 $CURRENT_PID
fi

JAR_NAME=$(ls -tr $BUILD_PATH | tail -n 1)
nohup java -jar $BUILD_PATH/$JAR_NAME > $HOME/spring-gift.log 2> $HOME/spring-gift-error.log &