package gift.product.exception;

import gift.common.exception.BusinessException;

public class ProductCreateException extends BusinessException {

    public static BusinessException EXCEPTION = new ProductCreateException();

    private ProductCreateException() {
        super(ProductErrorCode.PRODUCT_CREATE_FAILED);
    }
}