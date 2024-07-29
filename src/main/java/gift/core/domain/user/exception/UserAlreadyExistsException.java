package gift.core.domain.user.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class UserAlreadyExistsException extends APIException {
    public UserAlreadyExistsException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
