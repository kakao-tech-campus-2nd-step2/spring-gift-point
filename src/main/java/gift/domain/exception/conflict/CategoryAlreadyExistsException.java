package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class CategoryAlreadyExistsException extends ConflictException {

    public CategoryAlreadyExistsException() {
        super("This category name already exists. Try other one.", ErrorCode.CATEGORY_ALREADY_EXISTS);
    }
}
