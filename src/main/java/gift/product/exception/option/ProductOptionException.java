package gift.product.exception.option;

import org.springframework.http.HttpStatus;

public class ProductOptionException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ProductOptionException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
