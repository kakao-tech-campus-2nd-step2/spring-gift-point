package gift.exception;

import org.springframework.http.HttpStatus;

public class IllegalPointUseException extends CustomException {

    public IllegalPointUseException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
