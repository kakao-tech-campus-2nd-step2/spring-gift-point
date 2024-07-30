package gift.category.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class CategoryNotFoundException extends BusinessException {
    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
    }
}
