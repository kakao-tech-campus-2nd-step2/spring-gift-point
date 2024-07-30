package gift.exception;

import org.springframework.http.HttpStatus;

public class InvalidProductInfoException extends CustomException {

    public InvalidProductInfoException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
