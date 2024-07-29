package gift.core.domain.wishes.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class WishAlreadyExistsException extends APIException {
    public WishAlreadyExistsException() {
        super(ErrorCode.WISH_ALREADY_EXISTS);
    }
}
