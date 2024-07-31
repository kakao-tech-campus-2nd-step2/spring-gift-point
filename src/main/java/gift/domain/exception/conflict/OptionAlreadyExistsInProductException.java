package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class OptionAlreadyExistsInProductException extends ConflictException {

    public OptionAlreadyExistsInProductException() {
        super("The options already exists in product.", ErrorCode.OPTION_ALREADY_EXISTS_IN_PRODUCT);
    }
}
