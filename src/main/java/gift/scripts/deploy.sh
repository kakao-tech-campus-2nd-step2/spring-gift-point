#!/bin/bash

DEPLOY_DIR="/home/ubuntu/spring-gift-point"
REPO_LINK="https://github.com/Dockerel/spring-gift-point.git"
JAR_PATH="${DEPLOY_DIR}/build/libs/*.jar"
GIT_BRANCH="main"

APP_PID=$(pgrep -f $JAR_PATH)

if [ -z "$APP_PID" ]; then
  echo "No current running application"
else
  echo "Terminating application with PID: $APP_PID."
  kill -9 $APP_PID
  sleep 5
fi

if [ ! -d "${DEPLOY_DIR}" ]; then
  echo "Repository cloning..."
  git clone "${REPO_LINK}" "${DEPLOY_DIR}"
else
  echo "Repository updating..."
  cd "${DEPLOY_DIR}"
  git fetch --all
  git reset --hard origin/${GIT_BRANCH}
fi

echo "Compiling the application."
cd "${DEPLOY_DIR}"
chmod +x ./gradlew
./gradlew bootJar

echo "Launching the application."
cd build/libs/
nohup java -jar "${JAR_PATH}" &
echo "Application is now running in the background."