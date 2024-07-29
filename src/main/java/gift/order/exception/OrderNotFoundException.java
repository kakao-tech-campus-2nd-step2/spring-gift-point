package gift.order.exception;

import gift.common.exception.BusinessException;

public class OrderNotFoundException extends BusinessException {

    public static BusinessException EXCEPTION = new OrderNotFoundException();

    private OrderNotFoundException() {
        super(OrderErrorCode.ORDER_NOT_FOUND);
    }
}
