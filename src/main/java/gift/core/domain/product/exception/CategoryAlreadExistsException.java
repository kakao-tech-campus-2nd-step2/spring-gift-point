package gift.core.domain.product.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class CategoryAlreadExistsException extends APIException {
    public CategoryAlreadExistsException() {
        super(ErrorCode.PRODUCT_CATEGORY_ALREADY_EXISTS);
    }
}
