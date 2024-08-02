# spring-gift-point

## step1- API 명세

### 기능 요구 사항
### :지금까지 만든 선물하기 서비스를 배포하고 클라이언트와 연동할 수 있어야 한다.

#### 지속적인 배포를 위한 배포 스크립트를 작성한다.
#### 클라이언트와 API 연동 시 발생하는 보안 문제에 대응한다.
#### 서버와 클라이언트의 Origin이 달라 요청을 처리할 수 없는 경우를 해결한다.
#### HTTPS는 필수는 아니지만 팀 내에서 논의하고 필요한 경우 적용한다.

###
### 배포 스크립트
```
#!/bin/bash

# 프로젝트 경로 설정
PROJECT_PATH=/home/ubuntu/spring-gift-point
BUILD_PATH=$(ls $PROJECT_PATH/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

# 현재 실행 중인 애플리케이션 PID 가져오기
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 실행 중인 애플리케이션 종료
if [ -z $CURRENT_PID ]
then
  echo "현재 실행 중인 애플리케이션이 없습니다."
else
  echo "현재 실행 중인 애플리케이션 종료: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 배포할 경로 설정
DEPLOY_PATH=/home/ubuntu/

# 새 빌드를 배포 경로로 복사
echo "새 빌드를 배포 경로로 복사: $BUILD_PATH -> $DEPLOY_PATH"
cp $BUILD_PATH $DEPLOY_PATH
cd $DEPLOY_PATH

# 배포할 JAR 파일 설정
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME

# 애플리케이션 백그라운드 실행
echo "애플리케이션 백그라운드 실행: $DEPLOY_JAR"
nohup java -jar $DEPLOY_JAR > /dev/null 2> /dev/null < /dev/null &

```
