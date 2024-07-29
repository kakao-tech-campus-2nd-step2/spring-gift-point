package gift.core.exception.validation;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class InvalidArgumentException extends APIException {
    public InvalidArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
