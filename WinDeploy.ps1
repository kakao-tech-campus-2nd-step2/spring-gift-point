$privateKeyPath = "D:\aws_key\GDSC.pem"
$sourceJarPath = ".\build\libs\spring-gift-0.0.1-SNAPSHOT.jar"
$sourceScriptPath = ".\src\deploy.sh"
$remoteUser = "ubuntu"
$remoteHost = "15.165.67.223"
$remotePath = "~/gift"

function Check-Error {
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error occurred: Exit code $LASTEXITCODE"
        exit $LASTEXITCODE
    }
}

./gradlew bootJar

scp -i ${privateKeyPath} ${sourceJarPath} ${remoteUser}@${remoteHost}:${remotePath}
Check-Error

scp -i ${privateKeyPath} ${sourceScriptPath} ${remoteUser}@${remoteHost}:~
Check-Error

ssh -i ${privateKeyPath} ${remoteUser}@${remoteHost}