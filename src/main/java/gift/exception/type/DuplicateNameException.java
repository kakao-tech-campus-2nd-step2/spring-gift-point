package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class DuplicateNameException extends ApplicationException {
        public DuplicateNameException(String message) {
            super(message, HttpStatus.CONFLICT.value());
        }
}
