#!/bin/sh

# Set Variable
REPOSITORY_PATH=/home/ubuntu/repository/spring-gift-point
BUILD_PATH=$(ls $REPOSITORY_PATH/build/libs/*.jar)
DEPLOY_PATH=/home/ubuntu/deploy/
JAR_NAME=$(basename $BUILD_PATH)

# kill process
CURRENT_PID = $(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
        sleep 1
else
        kill -15 $CURRENT_PID
        sleep 5
fi

# build
REPOSITORY_PATH=/home/ubuntu/repository/spring-gift-point/
cd $REPOSITORY_PATH
./gradlew bootJar


# deploy
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR &
