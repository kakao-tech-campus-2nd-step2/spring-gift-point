package gift.domain.wishlist.exception;

import gift.global.exception.NotFoundException;

public class WishNotFoundException extends NotFoundException {

    public WishNotFoundException(String message) {
        super(message);
    }
}
