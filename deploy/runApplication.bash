#!/bin/bash

wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add -
sudo add-apt-repository 'deb https://apt.corretto.aws stable main'
sudo apt-get update; sudo apt-get install -y java-21-amazon-corretto-jdk

nohup java -jar spring-gift-0.0.1-SNAPSHOT.jar &