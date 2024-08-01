package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
