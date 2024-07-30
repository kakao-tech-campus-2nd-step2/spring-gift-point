package gift.exception;

import org.springframework.http.HttpStatus;

public class InvalidUserInfoException extends CustomException {

    public InvalidUserInfoException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
