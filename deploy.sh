echo "> git pull origin ps9319"
git pull origin ps9319

echo "./gradlew bootJar"
./gradlew bootJar

echo "cd build/libs"
cd build/libs

echo "java -jar spring-gift-0.0.1-SNAPSHOT.jar &"
java -jar spring-gift-0.0.1-SNAPSHOT.jar &
