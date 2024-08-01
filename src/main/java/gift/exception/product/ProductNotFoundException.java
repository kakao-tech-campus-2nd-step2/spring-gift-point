package gift.exception.product;

import static gift.exception.ErrorCode.PRODUCT_NOT_FOUND;

import gift.exception.CustomException;

public class ProductNotFoundException extends CustomException {

    public ProductNotFoundException() {
        super(PRODUCT_NOT_FOUND);
    }
}
