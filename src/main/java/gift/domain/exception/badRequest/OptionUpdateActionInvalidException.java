package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;
import gift.global.WebConfig.Constants.Domain.Option;

public class OptionUpdateActionInvalidException extends BadRequestException {

    public OptionUpdateActionInvalidException() {
        super(ErrorCode.OPTION_UPDATE_ACTION_INVALID);
    }
}
