package gift.exception.wish;

import gift.exception.WishlistException;
import org.springframework.http.HttpStatus;

public class DuplicateWishException extends WishlistException {

    public DuplicateWishException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
