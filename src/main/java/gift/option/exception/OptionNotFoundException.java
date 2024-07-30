package gift.option.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class OptionNotFoundException extends BusinessException {
    public OptionNotFoundException() {
        super(ErrorCode.OPTION_NOT_FOUND_ERROR);
    }
}
