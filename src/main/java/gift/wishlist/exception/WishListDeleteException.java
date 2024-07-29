package gift.wishlist.exception;

import gift.common.exception.BusinessException;

public class WishListDeleteException extends BusinessException {

    public static final BusinessException EXCEPTION = new WishListDeleteException();

    private WishListDeleteException() {
        super(WishListErrorCode.WISH_LIST_DELETE_FAILED);
    }
}
