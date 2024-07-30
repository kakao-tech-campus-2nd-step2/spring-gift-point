package gift.core.domain.product.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class CategoryNotFoundException extends APIException {
    public CategoryNotFoundException() {
        super(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND);
    }
}
