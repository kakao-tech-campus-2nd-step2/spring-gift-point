package gift.product.exception;

import gift.common.exception.BusinessException;

public class ProductUpdateException extends BusinessException {

    public static BusinessException EXCEPTION = new ProductUpdateException();

    private ProductUpdateException() {
        super(ProductErrorCode.PRODUCT_UPDATE_FAILED);
    }
}
