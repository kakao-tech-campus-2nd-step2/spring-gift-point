package gift.option.exception;

import gift.common.exception.BusinessException;

public class OptionNotEnoughStockException extends BusinessException {

    public static BusinessException EXCEPTION = new OptionNotEnoughStockException();

    private OptionNotEnoughStockException() {
        super(OptionErrorCode.NOT_ENOUGH_STOCK);
    }
}
