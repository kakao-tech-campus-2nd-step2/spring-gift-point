ssh -tt  -i gift-key.pem ubuntu@13.125.131.90 <<EOF
cd repo
git clone https://github.com/Daolove0323/spring-gift-point
cd spring-gift-point
./gradlew bootJar
cd build/libs
java -jar spring-gift-0.0.1-SNAPSHOT.jar &
exit
EOF
