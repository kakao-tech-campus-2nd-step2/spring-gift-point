package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ConcurrencyException extends ApplicationException {
    public ConcurrencyException(String message) {
        super(message, HttpStatus.CONTINUE.value());
    }
}
