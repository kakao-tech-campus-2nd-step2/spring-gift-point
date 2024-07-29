package gift.core.domain.user.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class UserNotFoundException extends APIException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
