package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;
import gift.global.WebConfig.Constants.Domain.Option;

public class OptionQuantityOutOfRangeException extends BadRequestException {

    public OptionQuantityOutOfRangeException() {
        super("The option quantity must be greater than or equal to "
            + Option.QUANTITY_RANGE_MIN
            + " and less than "
            + Option.QUANTITY_RANGE_MAX
            + ".",
            ErrorCode.OPTION_QUANTITY_OUT_OF_RANGE);
    }
}
