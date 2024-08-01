package gift.wishlist.exception;

import gift.common.exception.BusinessException;

public class WishListAlreadyExistsException extends BusinessException {

    public static final BusinessException EXCEPTION = new WishListAlreadyExistsException();

    private WishListAlreadyExistsException() {
        super(WishListErrorCode.WISH_LIST_ALREADY_EXISTS);
    }
}
