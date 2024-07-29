package gift.wishlist.exception;

import gift.common.exception.BusinessException;

public class WishListNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new WishListNotFoundException();

    private WishListNotFoundException() {
        super(WishListErrorCode.WISH_LIST_NOT_FOUND);
    }
}
