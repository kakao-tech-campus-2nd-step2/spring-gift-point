package gift.category.exception;

import gift.common.exception.BusinessException;

public class CategoryCreateException extends BusinessException {

    public static BusinessException EXCEPTION = new CategoryCreateException();

    private CategoryCreateException() {
        super(CategoryErrorCode.CATEGORY_CREATE_FAILED);
    }
}
