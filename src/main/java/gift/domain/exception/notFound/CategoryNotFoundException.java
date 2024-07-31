package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        super("category", ErrorCode.CATEGORY_NOT_FOUND);
    }
}
