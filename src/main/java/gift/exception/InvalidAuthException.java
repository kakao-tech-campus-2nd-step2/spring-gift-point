package gift.exception;

import org.springframework.http.HttpStatus;

public class InvalidAuthException extends CustomException {

    public InvalidAuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
