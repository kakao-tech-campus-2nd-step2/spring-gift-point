#!/bin/bash

DEPLOY_PATH=/home/ubuntu/build
JAR_NAME=$(basename $DEPLOY_PATH/*.jar)

CURRENT_PID=$(pgrep -f "$JAR_NAME")

if [ -z $CURRENT_PID ]
then
  sleep 1
else
  kill -15 $CURRENT_PID
  sleep 5
fi

cd $DEPLOY_PATH
nohup java -jar $JAR_NAME &
