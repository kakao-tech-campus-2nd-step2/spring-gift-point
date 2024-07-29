package gift.core.domain.user.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class UserAccountNotFoundException extends APIException {
    public UserAccountNotFoundException() {
        super(ErrorCode.USER_ACCOUNT_NOT_FOUND);
    }
}
