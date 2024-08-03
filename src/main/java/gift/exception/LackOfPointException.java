package gift.exception;

import org.springframework.http.HttpStatus;

public class LackOfPointException extends CustomException {

    public LackOfPointException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
