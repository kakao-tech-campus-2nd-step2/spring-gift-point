package gift.product.exception;

import gift.util.CustomException;
import gift.util.ErrorCode;

public class ProductException extends CustomException {

    public ProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}


