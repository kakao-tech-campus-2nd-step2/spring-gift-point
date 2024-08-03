# spring-gift-point

## step 0

지난 주차 코드 옮겨오기
---

---

## step 1

API 명세

- [x] 팀 내에서 결정한 API를 기준으로 기존 코드 검토 및 변경 사항 반영.

---

## step 2

배포 하기

- [x] 테스트 확인
- [x] 배포 스크립트 작성
```bash
#!/bin/bash
# 변수 설정
APP_NAME="spring-gift"
PROJECT_ROOT="$HOME/spring-gift-point"
DEPLOY_PATH="$HOME/deploy/$APP_NAME"
JAR_FILE="$PROJECT_ROOT/build/libs/$APP_NAME-0.0.1-SNAPSHOT.jar"
CONFIG_FILE="$PROJECT_ROOT/src/main/resources/application.properties"
CONFIG_BACKUP="$HOME/application.properties.backup"

# 프로젝트 디렉토리로 이동
cd $PROJECT_ROOT || { echo "Error: Project directory not found at $PROJECT_ROOT"; exit 1; }

# 설정 파일 백업
echo "Backing up configuration file..."
cp $CONFIG_FILE $CONFIG_BACKUP

# 최신 코드 가져오기
echo "Pulling latest changes from git..."
git pull origin main

# 설정 파일 복원
echo "Restoring configuration file..."
cp $CONFIG_BACKUP $CONFIG_FILE

# 빌드 (테스트 제외)
echo "Building the application..."
if [ -f "./gradlew" ]; then
chmod +x ./gradlew
./gradlew clean build -x test
else
echo "Error: gradlew not found in $PROJECT_ROOT. Make sure you're in the project root directory."
exit 1
fi

# 실행 중인 애플리케이션 종료
echo "Stopping the current application..."
pkill -f $APP_NAME

# JAR 파일 복사
echo "Copying JAR file to deploy path..."
mkdir -p $DEPLOY_PATH
cp $JAR_FILE $DEPLOY_PATH/

# 애플리케이션 시작
echo "Starting the application..."
nohup java -jar $DEPLOY_PATH/$(basename $JAR_FILE) > $DEPLOY_PATH/app.log 2>&1 &

echo "Deployment completed!"
```
- [x] CORS 작성
- [x] EC2 생성
- [x] 배포 하기
  - http://43.201.148.248:8080/admin?page=0&size=10&sort=createdDate,asc&categoryId=1