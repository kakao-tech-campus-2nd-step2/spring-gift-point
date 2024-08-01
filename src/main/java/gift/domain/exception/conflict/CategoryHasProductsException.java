package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class CategoryHasProductsException extends ConflictException {

    public CategoryHasProductsException() {
        super(ErrorCode.CATEGORY_HAS_PRODUCTS);
    }
}
