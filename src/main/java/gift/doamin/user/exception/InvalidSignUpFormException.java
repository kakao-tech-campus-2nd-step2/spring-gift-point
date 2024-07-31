package gift.doamin.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidSignUpFormException extends ResponseStatusException {

    public InvalidSignUpFormException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
