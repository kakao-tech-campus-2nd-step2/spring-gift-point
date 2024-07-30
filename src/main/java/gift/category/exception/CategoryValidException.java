package gift.category.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class CategoryValidException extends BusinessException {
    public CategoryValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
