package gift.core.domain.order.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class PointNotEnoughException extends APIException {
    public PointNotEnoughException() {
        super(ErrorCode.POINT_NOT_ENOUGH);
    }
}
