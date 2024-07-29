package gift.common.exception;

import org.springframework.http.HttpStatus;

public class CategoryException extends RuntimeException{

    private final HttpStatus httpStatus;

    public CategoryException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
