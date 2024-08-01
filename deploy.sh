#!/bin/bash
BUILD_PATH=$(find /home/ubuntu/repository/spring-gift-point/build/libs -type f -name "*.jar" | grep -v "plain" | head -n 1)
JAR_NAME=$(basename $BUILD_PATH)
echo "build_path: $BUILD_PATH"
echo "jar_name: $JAR_NAME"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  sleep 1
else
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_PATH=/home/ubuntu/
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

java -jar $JAR_NAME &
