package gift.domain.category.exception;

import gift.global.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
