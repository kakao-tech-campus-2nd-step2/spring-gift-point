package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidProductOptionException extends ApplicationException {
    public InvalidProductOptionException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
