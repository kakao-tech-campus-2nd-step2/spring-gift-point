package gift.exception.customException;

import gift.exception.ErrorCode;

public class CustomOutOfStockException extends CustomException {

    public CustomOutOfStockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
