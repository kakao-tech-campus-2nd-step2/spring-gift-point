package gift.category.exception;

import gift.common.exception.BusinessException;

public class CategoryAlreadyExistsException extends BusinessException {

    public static BusinessException EXCEPTION = new CategoryAlreadyExistsException();

    private CategoryAlreadyExistsException() {
        super(CategoryErrorCode.CATEGORY_ALREADY_EXISTS);
    }
}
