package gift.exception.customException;

import gift.exception.ErrorCode;

public class AccessDeniedException extends CustomException {

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
