package gift.product.exception;

import gift.common.exception.BusinessException;

public class ProductNotFoundException extends BusinessException {

    public static BusinessException EXCEPTION = new ProductNotFoundException();

    private ProductNotFoundException() {
        super(ProductErrorCode.PRODUCT_NOT_FOUND);
    }
}
