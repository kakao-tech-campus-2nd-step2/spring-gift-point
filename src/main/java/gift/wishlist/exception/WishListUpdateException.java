package gift.wishlist.exception;

import gift.common.exception.BusinessException;

public class WishListUpdateException extends BusinessException {

    public static final BusinessException EXCEPTION = new WishListUpdateException();

    private WishListUpdateException() {
        super(WishListErrorCode.WISH_LIST_UPDATE_FAILED);
    }
}
