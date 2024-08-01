package gift.exception;

import org.springframework.http.HttpStatus;

public class ProductException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ProductException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}