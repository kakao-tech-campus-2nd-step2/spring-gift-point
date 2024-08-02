#!/bin/bash

# 변수 설정
PEM_FILE="mogld22.pem"
EC2_USER="ubuntu"
EC2_HOST="43.203.211.211"  # EC2 인스턴스의 퍼블릭 IP
APP_NAME="spring-gift-0.0.1-SNAPSHOT.jar"
REMOTE_DIR="repo/spring-gift-point/"

# 1. 로컬에서 빌드
echo "로컬에서 빌드를 시작합니다..."
./gradlew bootJar

# 2. EC2 서버에 배포 파일 전송
echo "EC2 서버에 배포 파일을 전송합니다..."
scp -i $PEM_FILE build/libs/$APP_NAME $EC2_USER@$EC2_HOST:$REMOTE_DIR/

# 3. EC2 서버에서 애플리케이션 실행
echo "EC2 서버에서 애플리케이션을 실행합니다..."
ssh -i $PEM_FILE $EC2_USER@$EC2_HOST <<EOF
  pkill -f $APP_NAME || true
  cd $REMOTE_DIR
  java -jar $APP_NAME > app.log 2>&1 &
  echo "애플리케이션이 성공적으로 시작되었습니다."
EOF

echo "배포가 완료되었습니다."