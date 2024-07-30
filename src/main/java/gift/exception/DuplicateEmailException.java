package gift.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends CustomException {

    public DuplicateEmailException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
