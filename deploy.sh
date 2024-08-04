#!/bin/bash

echo "> Git Pull"
REPOSITORY=/home/ubuntu/git/spring-gift-point/
cd $REPOSITORY
git pull

echo "> Build Project"
./gradlew bootJar

BUILD_PATH=$(ls $REPOSITORY/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

echo "> Terminate Existing Application"
CURRENT_PID=$(pgrep -f $JAR_NAME)
if [ -z $CURRENT_PID ]
then
  echo "  Pass"
  sleep 1
else
  echo "  Kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/deploy/
cp $BUILD_PATH $DEPLOY_PATH

echo "> Deploy Application"
cd $DEPLOY_PATH
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR > log.out 2>&1 &