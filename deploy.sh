#!/bin/bash

BUILD_DIR="/home/ubuntu/spring-gift-point/build/libs"
DEPLOY_DIR="/home/ubuntu"
LOG_FILE="/home/ubuntu/deploy.log"

BUILD_PATH=$(ls $BUILD_DIR/*.jar 2>/dev/null)
if [ -z "$BUILD_PATH" ]; then
  echo "No JAR file found in $BUILD_DIR" | tee -a $LOG_FILE
  exit 1
fi

JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -n "$CURRENT_PID" ]; then
  echo "Stopping application with PID: $CURRENT_PID" | tee -a $LOG_FILE
  kill -15 $CURRENT_PID
  sleep 5
else
  echo "No running application found." | tee -a $LOG_FILE
fi

echo "Copying $BUILD_PATH to $DEPLOY_DIR" | tee -a $LOG_FILE
cp $BUILD_PATH $DEPLOY_DIR
if [ $? -ne 0 ]; then
  echo "Failed to copy JAR file to $DEPLOY_DIR" | tee -a $LOG_FILE
  exit 1
fi

cd $DEPLOY_DIR

DEPLOY_JAR=$DEPLOY_DIR/$JAR_NAME
echo "Starting application $DEPLOY_JAR" | tee -a $LOG_FILE
nohup java -jar $DEPLOY_JAR > $LOG_FILE 2>&1 &
if [ $? -eq 0 ]; then
  echo "Application started successfully." | tee -a $LOG_FILE
else
  echo "Failed to start the application." | tee -a $LOG_FILE
  exit 1
fi
