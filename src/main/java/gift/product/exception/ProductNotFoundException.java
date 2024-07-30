package gift.product.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND_ERROR);
    }
}
