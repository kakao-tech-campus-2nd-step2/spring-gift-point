package gift.option.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class OptionNotEnoughException extends BusinessException {
    public OptionNotEnoughException() {
        super(ErrorCode.OPTION_NOT_ENOUGH_ERROR);
    }
}
