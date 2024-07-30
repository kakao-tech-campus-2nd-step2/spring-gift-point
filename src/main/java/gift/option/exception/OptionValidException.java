package gift.option.exception;

import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorCode;

public class OptionValidException extends DomainValidationException {
    public OptionValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
