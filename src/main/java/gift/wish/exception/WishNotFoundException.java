package gift.wish.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class WishNotFoundException extends BusinessException {
    public WishNotFoundException() {
        super(ErrorCode.WISH_NOT_FOUND_ERROR);
    }
}
