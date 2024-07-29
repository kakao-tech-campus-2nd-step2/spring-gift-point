package gift.product.exception;

import gift.common.exception.BusinessException;

public class ProductDeleteException extends BusinessException {

    public static BusinessException EXCEPTION = new ProductDeleteException();

    private ProductDeleteException() {
        super(ProductErrorCode.PRODUCT_DELETE_FAILED);
    }
}
