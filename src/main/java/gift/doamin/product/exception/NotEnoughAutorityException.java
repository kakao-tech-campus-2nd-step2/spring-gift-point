package gift.doamin.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotEnoughAutorityException extends ResponseStatusException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public NotEnoughAutorityException() {
        super(STATUS);
    }

    public NotEnoughAutorityException(String reason) {
        super(STATUS, reason);
    }
}
