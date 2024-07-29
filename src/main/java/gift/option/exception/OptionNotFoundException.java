package gift.option.exception;

import gift.common.exception.BusinessException;

public class OptionNotFoundException extends BusinessException {

    public static BusinessException EXCEPTION = new OptionNotFoundException();

    private OptionNotFoundException() {
        super(OptionErrorCode.OPTION_NOT_FOUND);
    }
}
