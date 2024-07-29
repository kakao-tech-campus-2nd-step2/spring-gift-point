package gift.exception.customException;

import gift.exception.ErrorCode;

public class CustomDuplicateException extends CustomException {

    public CustomDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
