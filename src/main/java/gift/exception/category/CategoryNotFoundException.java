package gift.exception.category;

import gift.exception.CategoryException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends CategoryException {

    public CategoryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
