#!/bin/bash

# copy this file your home directory, change CLONE_PATH, and run it.

CLONE_PATH="/home/ubuntu/spring-gift-point"
CLONE_URL="https://github.com/lja3723/spring-gift-point"
BRANCH="step2"
ENV_FILE="/home/ubuntu/.env"

stop_application() {
    echo "[$0] >> Stopping application..."

    # Using ps and grep to find the process
    CURRENT_PID=$(ps aux | grep "java -jar .*${JAR_NAME}" | grep -v grep | awk '{print $2}')

    if [ -z "$CURRENT_PID" ]; then
        echo "[$0] >> There are no running applications."
    else
        echo "[$0] >> Terminate application PID: $CURRENT_PID"
        kill -15 "$CURRENT_PID"
        sleep 5
        # Verify if the process was terminated
        if [ -z "$(ps -p $CURRENT_PID -o pid=)" ]; then
            echo "[$0] >> Application stopped."
        else
            echo "[$0] >> Failed to stop the application."
        fi
    fi
}

start_application() {
    echo "[$0] >> Starting application..."

    # Stop any running application before starting a new one
    stop_application

    # Clone or fetch source code
    if [ ! -d "${CLONE_PATH}" ]; then
        echo "[$0] >> Cloning git repository..."
        git clone "${CLONE_URL}" "${CLONE_PATH}"
    else
        echo "[$0] >> Fetching modifying features..."
        cd "${CLONE_PATH}"

        # Fetch latest changes
        git fetch

        # Check if there are changes
        LOCAL_COMMIT=$(git rev-parse ${BRANCH})
        REMOTE_COMMIT=$(git rev-parse origin/${BRANCH})

        if [ "$LOCAL_COMMIT" != "$REMOTE_COMMIT" ]; then
            echo "[$0] >> Local branch is outdated. Pulling latest changes..."
            git checkout "${BRANCH}"
            git pull origin "${BRANCH}"

            # Build and run the application
            echo "[$0] >> Building application..."
            chmod +x ./gradlew
            ./gradlew bootJar
        else
            echo "[$0] >> No changes detected. Skipping build."
        fi
    fi

    # Get the .jar file path and name
    JAR_PATH=$(ls ${CLONE_PATH}/build/libs/*.jar)
    JAR_NAME=$(basename "${JAR_PATH}")

    echo "[$0] >> Running application..."
    cd "${CLONE_PATH}/build/libs"
    nohup java -jar "${JAR_NAME}" &
    echo "[$0] >> Application started. Execute \"tail -f ${CLONE_PATH}/build/libs/nohup.out\" to look at the server log."
}

# Check if .env file exists
if [ ! -f "$ENV_FILE" ]; then
    echo "[$0] >> File ${ENV_FILE} not found!"
    echo "[$0] >> You should include JWT_SECRET_KEY, KAKAO_API_KEY in the \"${ENV_FILE}\"."
    echo "[$0] >> Exiting..."
    exit 1
fi

# Load environment variables from .env file
export $(grep -v '^#' "${ENV_FILE}" | xargs)

case "$1" in
    start)
        start_application
        ;;
    stop)
        stop_application
        ;;
    *)
        echo "[$0] >> Usage: $0 {start|stop}"
        exit 1
        ;;
esac
