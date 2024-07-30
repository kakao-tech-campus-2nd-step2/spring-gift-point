package gift.common.exception;

import gift.category.CategoryErrorCode;
import org.springframework.http.HttpStatus;

public class CategoryException extends RuntimeException {

    private final CategoryErrorCode categoryErrorCode;
    private final HttpStatus httpStatus;

    public CategoryException(CategoryErrorCode categoryErrorCode) {
        super(categoryErrorCode.getMessage());
        this.categoryErrorCode = categoryErrorCode;
        this.httpStatus = categoryErrorCode.getHttpStatus();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
