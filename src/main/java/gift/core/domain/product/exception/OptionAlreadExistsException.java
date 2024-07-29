package gift.core.domain.product.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class OptionAlreadExistsException extends APIException {
    public OptionAlreadExistsException() {
        super(ErrorCode.OPTION_ALREADY_EXISTS);
    }
}
