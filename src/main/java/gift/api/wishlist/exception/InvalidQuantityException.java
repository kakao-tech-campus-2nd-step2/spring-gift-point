package gift.api.wishlist.exception;

import gift.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class InvalidQuantityException extends GlobalException {

    private static final String MESSAGE = "Quantity must be more than 0.";

    public InvalidQuantityException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
