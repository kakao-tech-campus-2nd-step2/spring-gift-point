package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidOptionQuantityException extends ApplicationException {
    public InvalidOptionQuantityException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
