#!/bin/bash

BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ];
then
  echo "No application is running."
  sleep 1
else
  echo "Stopping application with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR &
