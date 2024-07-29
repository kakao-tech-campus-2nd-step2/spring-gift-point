package gift.core.domain.product.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class OptionNotFoundException extends APIException {
    public OptionNotFoundException() {
        super(ErrorCode.OPTION_NOT_FOUND);
    }
}
