package gift.exception.wish;

import static gift.exception.ErrorCode.WISH_NOT_FOUND;

import gift.domain.Wishlist;
import gift.exception.CustomException;

public class WishNotFoundException extends CustomException {

    public WishNotFoundException() {
        super(WISH_NOT_FOUND);
    }
}
