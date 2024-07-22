package gift.product.exception.category;

import org.springframework.http.HttpStatus;

public class CategoryException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CategoryException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
