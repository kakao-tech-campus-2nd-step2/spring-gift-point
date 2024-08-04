package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class DuplicateException extends ApplicationException {
        public DuplicateException(String message) {
            super(message, HttpStatus.CONFLICT.value());
        }
}
