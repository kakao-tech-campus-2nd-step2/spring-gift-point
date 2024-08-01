package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class CategoryAlreadyExistsException extends ConflictException {

    public CategoryAlreadyExistsException() {
        super(ErrorCode.CATEGORY_ALREADY_EXISTS);
    }
}
