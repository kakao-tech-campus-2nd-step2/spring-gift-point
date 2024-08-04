#!/bin/bash

# Java 21 설치 함수
install_java() {
  echo "Installing java 21..."
  sudo apt update
  sudo apt install -y openjdk-21-jdk
  echo "Java 21 installed."
}

# Docker 설치 함수
install_docker() {
  echo "Installing Docker..."
  sudo apt-get remove docker docker-engine docker.io containerd runc -y
  sudo apt-get update
  sudo apt-get install apt-transport-https ca-certificates curl software-properties-common -y
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  sudo apt-get update
  sudo apt-get install docker-ce docker-ce-cli containerd.io -y
  echo "Docker installed."
}

# Docker Compose 설치 함수
install_docker_compose() {
  echo "Installing Docker Compose..."
  sudo curl -L "https://github.com/docker/compose/releases/download/v2.13.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
  sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
  echo "Docker Compose installed."
}

# Java 21 존재 여부 체크
if type -p java; then
  echo "Java found in PATH."
  _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]]; then
  echo "Java found in JAVA_HOME."
  _java="$JAVA_HOME/bin/java"
else
  echo "Java not found."
  install_java
fi


# Docker 존재 여부 체크
if ! [ -x "$(command -v docker)" ]; then
  echo "Docker is not installed."
  install_docker
else
  echo "Docker is already installed."
fi

# Docker Compose 존재 여부 체크
if ! [ -x "$(command -v docker-compose)" ]; then
  echo "Docker Compose is not installed."
  install_docker_compose
else
  echo "Docker Compose is already installed."
fi

# 설치된 버전 확인
java -version
docker --version
docker-compose --version