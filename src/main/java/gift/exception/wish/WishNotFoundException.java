package gift.exception.wish;

import gift.exception.WishlistException;
import org.springframework.http.HttpStatus;

public class WishNotFoundException extends WishlistException {

    public WishNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
