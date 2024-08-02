package gift.exception.order;

import gift.exception.BaseErrorCode;
import gift.exception.CustomException;

public class OrderCustomException extends CustomException {
    public OrderCustomException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public static class ExceedsPointLimitException extends OrderCustomException {
        public ExceedsPointLimitException() {
            super(OrderErrorCode.POINT_USAGE_EXCEEDS_LIMIT);
        }
    }

    public static class InsufficientPointsException extends OrderCustomException {
        public InsufficientPointsException() {
            super(OrderErrorCode.INSUFFICIENT_POINTS);
        }
    }
}
