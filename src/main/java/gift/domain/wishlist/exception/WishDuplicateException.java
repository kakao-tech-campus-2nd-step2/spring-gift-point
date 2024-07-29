package gift.domain.wishlist.exception;

import gift.global.exception.DuplicateException;

public class WishDuplicateException extends DuplicateException {

    public WishDuplicateException(String message) {
        super(message);
    }
}
