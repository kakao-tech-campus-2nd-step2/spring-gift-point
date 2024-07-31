package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class CategoryHasProductsException extends ConflictException {

    public CategoryHasProductsException() {
        super("This category cannot be deleted because some products are included in it.", ErrorCode.CATEGORY_HAS_PRODUCTS);
    }
}
