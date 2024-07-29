package gift.common.exception;

import org.springframework.http.HttpStatus;

public class ProductException extends RuntimeException{

    private final HttpStatus httpStatus;

    public ProductException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
