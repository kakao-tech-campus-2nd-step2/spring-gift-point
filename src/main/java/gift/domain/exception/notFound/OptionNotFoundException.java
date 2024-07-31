package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

public class OptionNotFoundException extends NotFoundException {

    public OptionNotFoundException() {
        super("option", ErrorCode.OPTION_NOT_FOUND);
    }
}
