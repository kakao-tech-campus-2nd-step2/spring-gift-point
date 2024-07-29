package gift.common.exception;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException{

    private final HttpStatus httpStatus;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
