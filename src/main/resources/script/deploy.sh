#!/bin/bash

# 1. 8080 포트 프로세스 확인 및 kill
fuser -k 8080/tcp

# 2. JAR 파일 실행 (백그라운드 실행 및 로그 출력)
nohup java -jar /home/ubuntu/repository/spring-gift-0.0.1-SNAPSHOT.jar > /home/ubuntu/repository/output.log 2>&1 &
