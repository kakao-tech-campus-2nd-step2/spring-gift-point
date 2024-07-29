package gift.exception.wish;

import gift.exception.WishlistException;
import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends WishlistException {

    public PermissionDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
