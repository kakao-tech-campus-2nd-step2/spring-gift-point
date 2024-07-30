package gift.option.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class DuplicateOptionNameException extends BusinessException {
    public DuplicateOptionNameException() {
        super(ErrorCode.DUPLICATE_OPTION_NAME_ERROR);
    }
}
