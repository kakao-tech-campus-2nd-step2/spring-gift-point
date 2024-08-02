cd ~/desktop/github
ssh -tt  -i gift-key.pem ubuntu@43.203.228.50 <<EOF
PROJECT_PATH=/home/ubuntu/repo/spring-gift-point
cd \$PROJECT_PATH
git pull

export PROJECT_PATH=$PATH:/usr/bin:/bin
killall -9 java

./gradlew clean bootJar
cd build/libs
sudo java -jar spring-gift-0.0.1-SNAPSHOT.jar &
exit
EOF
