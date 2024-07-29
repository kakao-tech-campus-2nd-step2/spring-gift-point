package gift.category.exception;

import gift.common.exception.BusinessException;

public class CategoryUpdateException extends BusinessException {

    public static BusinessException EXCEPTION = new CategoryUpdateException();

    private CategoryUpdateException() {
        super(CategoryErrorCode.CATEGORY_UPDATE_FAILED);
    }
}
