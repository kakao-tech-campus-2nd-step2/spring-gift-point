#!/bin/bash

# 변수 설정
KEY_PATH="/Users/takjungmin/Desktop/AWS/key-wjdals.pem"
LOCAL_JAR_PATH="build/libs/spring-gift-0.0.1-SNAPSHOT.jar"
REMOTE_USER="ubuntu"
REMOTE_IP="13.125.233.182"
REMOTE_DIR="/home/ubuntu/app/build"

# JAR 파일 업로드
scp -i $KEY_PATH $LOCAL_JAR_PATH $REMOTE_USER@$REMOTE_IP:$REMOTE_DIR

# SSH를 통해 JAR 파일 실행
ssh -i $KEY_PATH $REMOTE_USER@$REMOTE_IP "java -jar $REMOTE_DIR/$(basename $LOCAL_JAR_PATH)"

