package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class FieldNameIllegalException extends BadRequestException {

    public FieldNameIllegalException() {
        super(ErrorCode.FIELD_NAME_ILLEGAL);
    }
}
