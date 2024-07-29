package gift.category.exception;

import gift.common.exception.BusinessException;

public class CategoryNotFoundException extends BusinessException {

    public static BusinessException EXCEPTION = new CategoryNotFoundException();

    private CategoryNotFoundException() {
        super(CategoryErrorCode.CATEGORY_NOT_FOUND);
    }
}
