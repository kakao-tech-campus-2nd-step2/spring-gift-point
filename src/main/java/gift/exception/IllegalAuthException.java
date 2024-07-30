package gift.exception;

import org.springframework.http.HttpStatus;

public class IllegalAuthException extends CustomException {

    public IllegalAuthException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
