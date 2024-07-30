package gift.exception;

import org.springframework.http.HttpStatus;

public class InvalidOptionInfoException extends CustomException {

    public InvalidOptionInfoException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
