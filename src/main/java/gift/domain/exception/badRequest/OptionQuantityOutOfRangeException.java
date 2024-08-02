package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;
import gift.global.WebConfig.Constants.Domain.Option;

public class OptionQuantityOutOfRangeException extends BadRequestException {

    public OptionQuantityOutOfRangeException() {
        super(ErrorCode.OPTION_QUANTITY_OUT_OF_RANGE);
    }
}
