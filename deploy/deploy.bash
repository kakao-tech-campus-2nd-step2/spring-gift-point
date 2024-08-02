#!/bin/bash

# Configure
KEY_PAIR=$KEY_PAIR
INSTANCE_IP=$INSTANCE_IP
REMOTE_USER="ubuntu"
LOCAL_BUILD_FILE_PATH="./build/libs/spring-gift-0.0.1-SNAPSHOT.jar"
LOCAL_RUN_APPLICATION_FILE_PATH="./deploy/runApplication.bash"
REMOTE_BUILD_FILE_PATH="/home/ubuntu"

# Build
cd ..
./gradlew build

# Deploy
scp -i $KEY_PAIR $LOCAL_BUILD_FILE_PATH $LOCAL_RUN_APPLICATION_FILE_PATH $REMOTE_USER@$INSTANCE_IP:$REMOTE_BUILD_FILE_PATH

ssh -i $KEY_PAIR $REMOTE_USER@$INSTANCE_IP bash runApplication.bash