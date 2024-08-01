package gift.exception.product;

import gift.exception.BaseErrorCode;
import gift.exception.CustomException;

public class ProductCustomException extends CustomException {
    public ProductCustomException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public static class ProductMustHaveOptionException extends ProductCustomException {
        public ProductMustHaveOptionException() {
            super(ProductErrorCode.PRODUCT_MUST_HAVE_OPTION);
        }
    }

    public static class NotEnoughStockException extends ProductCustomException {
        public NotEnoughStockException() {
            super(ProductErrorCode.NOT_ENOUGH_STOCK);
        }
    }
}
