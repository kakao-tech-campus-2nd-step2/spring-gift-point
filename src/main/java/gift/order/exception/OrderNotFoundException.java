package gift.order.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException() {
        super(ErrorCode.OPTION_NOT_FOUND_ERROR);
    }
}
