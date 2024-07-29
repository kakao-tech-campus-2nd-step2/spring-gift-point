package gift.common.exception;

import org.springframework.http.HttpStatus;

public class WishException extends RuntimeException{

    private final HttpStatus httpStatus;

    public WishException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
