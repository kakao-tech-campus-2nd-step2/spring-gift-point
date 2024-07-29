package gift.exception;

import org.springframework.http.HttpStatus;

public class CategoryException extends ApplicationException {

    public CategoryException(String message, HttpStatus status) {
        super(message, status);
    }
}
