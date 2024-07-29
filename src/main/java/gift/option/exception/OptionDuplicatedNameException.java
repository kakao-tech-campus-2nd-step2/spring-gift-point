package gift.option.exception;

import gift.common.exception.BusinessException;

public class OptionDuplicatedNameException extends BusinessException {

    public static BusinessException EXCEPTION = new OptionDuplicatedNameException();

    private OptionDuplicatedNameException() {
        super(OptionErrorCode.DUPLICATED_OPTION_NAME);
    }
}
