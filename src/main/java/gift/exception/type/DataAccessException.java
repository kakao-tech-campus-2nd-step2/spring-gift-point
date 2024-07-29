package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class DataAccessException extends ApplicationException {

    public DataAccessException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
