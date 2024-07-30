package gift.user.exception;

import gift.util.CustomException;
import gift.util.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
