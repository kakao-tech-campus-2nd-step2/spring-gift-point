package gift.exception.customException;

import gift.exception.ErrorCode;

public class CustomNotFoundException extends CustomException {

    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
