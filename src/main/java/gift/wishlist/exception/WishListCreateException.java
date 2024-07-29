package gift.wishlist.exception;

import gift.common.exception.BusinessException;

public class WishListCreateException extends BusinessException {

    public static BusinessException EXCEPTION = new WishListCreateException();

    private WishListCreateException() {
        super(WishListErrorCode.WISH_LIST_ADD_FAILED);
    }
}
