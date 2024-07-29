package gift.core.domain.wishes.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class WishNotFoundException extends APIException {
    public WishNotFoundException() {
        super(ErrorCode.WISH_NOT_FOUND);
    }
}
