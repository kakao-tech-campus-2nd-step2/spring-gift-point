package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends ApplicationException {
    public UnAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
