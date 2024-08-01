package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class OptionAlreadyExistsInProductException extends ConflictException {

    public OptionAlreadyExistsInProductException() {
        super(ErrorCode.OPTION_ALREADY_EXISTS_IN_PRODUCT);
    }
}
