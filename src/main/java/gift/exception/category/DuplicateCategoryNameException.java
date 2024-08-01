package gift.exception.category;

import static gift.exception.ErrorCode.DUPLICATE_CATEGORY_NAME;

import gift.exception.CustomException;

public class DuplicateCategoryNameException extends CustomException {

    public DuplicateCategoryNameException() {
        super(DUPLICATE_CATEGORY_NAME);
    }
}
