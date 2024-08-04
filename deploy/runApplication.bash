#!/bin/bash

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/{print $2}')

if [[ $JAVA_VERSION =~ ^21 ]];
then
  sudo apt-get update
  sudo apt-get upgrade -y
else
  wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add -
  sudo add-apt-repository 'deb https://apt.corretto.aws stable main'
  sudo apt-get update;
  sudo apt-get install -y java-21-amazon-corretto-jdk
fi

SERVER_PID=$(ps aux | grep '[j]ava -jar' | awk 'NR==1 {print $2}')

if [ -n $SERVER_PID ];
then
    kill $SERVER_PID
fi

nohup java -jar spring-gift-0.0.1-SNAPSHOT.jar &