package gift.category.exception;

import gift.common.exception.BusinessException;

public class CategoryDeleteException extends BusinessException {

    public static BusinessException EXCEPTION = new CategoryDeleteException();

    private CategoryDeleteException() {
        super(CategoryErrorCode.CATEGORY_DELETE_FAILED);
    }
}
