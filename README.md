# spring-gift-point

### Step1

작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.

* 팀원들과 상의 후 API문서를 만들고 API, Request, Response를 통일하였습니다.
  * 이 과정에서 ResponseDTO, ErrorCode Enum 작성 및 기존 코드 리팩토링을 진행했습니다.
* API 문서 주소: http://43.201.17.220:8080/swagger-ui/index.html#/


### Step2

배포 스크립트를 작성하고 CORS를 위한 코드를 작성했습니다.

작성한 배포 스크립트는 아래와 같습니다!

```
#!/bin/sh

PROJECT_PATH=/home/ubuntu/repository
PROJECT_NAME=spring-gift-point
BUILD_PATH=build/libs
JAR_NAME=$(basename $PROJECT_PATH/$PROJECT_NAME/.*jar)

echo "\n 🐳    [$PROJECT_PATH/$PROJECT_NAME] 경로로 이동.\n"
cd $PROJECT_PATH/$PROJECT_NAME

echo " 🐳    최신 코드 PULL\n"
git pull

echo " 🐳    프로젝트 새로  빌드.\n"
./gradlew clean bootJar


PID=$(pgrep -f $JAR_NAME)

if [ -z $PID ]; then
        echo " 🎉     실행중인 애플리케   션이 없어서 곧바로 실행합니다.\n"

else
        echo " ❌     실행중인 애플리케이션이 있어서 이를 종료합니다. [PID = $PID]\n"
        kill -15 $PID
        sleep 5
fi

JAR_NAME=$(ls -tr $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH |grep .jar | tail -n 1)

echo ">JAR NAME: $JAR_NAME"

cd $PROJECT_PATH/$PROJECT_NAME/$BUILD_PATH

nohup java -jar $JAR_NAME &

~                              
```
