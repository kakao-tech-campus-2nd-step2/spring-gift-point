#!/bin/bash

BUILD_PATH=$(ls /home/ubuntu/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "No application running, so no need to stop."
else
  echo "Stopping application with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/spring-gift-point/app/
mkdir -p $DEPLOY_PATH
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "Deploying $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /home/ubuntu/spring-gift-point/build/libs/nohup.out 2>&1 &

tail -f /home/ubuntu/spring-gift-point/build/libs/nohup.out