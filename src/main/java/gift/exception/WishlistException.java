package gift.exception;

import org.springframework.http.HttpStatus;

public class WishlistException extends ApplicationException {

    public WishlistException(String message, HttpStatus status) {
        super(message, status);
    }
}
