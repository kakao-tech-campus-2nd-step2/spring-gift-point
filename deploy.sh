#!/bin/bash

REPO_DIR=/home/ubuntu/repository/spring-gift-point
echo "Updating the repository..."
cd $REPO_DIR
git pull

echo "Building the project..."
./gradlew bootJar

BUILD_PATH=$(ls /home/ubuntu/repository/spring-gift-point/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

echo "Checking for currently running application..."
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "No currently running application."
  sleep 1
else
  echo "Stopping application with PID: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/
echo "Deploying the new JAR file..."
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

echo "Deployment finished."