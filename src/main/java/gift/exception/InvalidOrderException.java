package gift.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderException extends CustomException {

    public InvalidOrderException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
