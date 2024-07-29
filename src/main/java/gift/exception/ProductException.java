package gift.exception;

import org.springframework.http.HttpStatus;

public class ProductException extends ApplicationException {

    public ProductException(String message, HttpStatus status) {
        super(message, status);
    }
}
