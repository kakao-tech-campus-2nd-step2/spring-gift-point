package gift.exception;

import org.springframework.http.HttpStatus;

public class DuplicateOptionNameException extends CustomException {

    public DuplicateOptionNameException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
