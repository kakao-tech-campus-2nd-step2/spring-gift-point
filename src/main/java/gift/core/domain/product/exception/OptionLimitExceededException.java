package gift.core.domain.product.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class OptionLimitExceededException extends APIException {
    public OptionLimitExceededException() {
        super(ErrorCode.OPTION_LIMIT_EXCEEDED);
    }
}
