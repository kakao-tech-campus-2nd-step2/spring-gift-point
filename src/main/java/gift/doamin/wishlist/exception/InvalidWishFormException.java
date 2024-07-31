package gift.doamin.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidWishFormException extends ResponseStatusException {

    public InvalidWishFormException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
