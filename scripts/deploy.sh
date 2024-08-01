#!/bin/bash
PROJECT_NAME="spring-gift-point"
DEPLOY_PATH="/home/ubuntu/repository/$PROJECT_NAME/"
DEPLOY_LOG_PATH="/home/ubuntu/repository/$PROJECT_NAME/deploy.log"

sudo echo "===== 배포 시작 : $(date +%c) =====" >> "$DEPLOY_LOG_PATH"

cd $DEPLOY_PATH

sudo git pull origin main

# 기존 컨테이너 종료 및 삭제
sudo echo "기존 컨테이너 종료 및 삭제 중..." >> "$DEPLOY_LOG_PATH"
sudo docker-compose down >> "$DEPLOY_LOG_PATH" 2>&1

# Docker Compose를 사용하여 빌드 및 배포
sudo echo "새로운 컨테이너 빌드 및 시작 중..." >> "$DEPLOY_LOG_PATH"
sudo docker-compose up --build -d >> "$DEPLOY_LOG_PATH" 2>&1

# 배포 완료 로그 기록
sudo echo "배포 완료 : $(date +%c)" >> "$DEPLOY_LOG_PATH"