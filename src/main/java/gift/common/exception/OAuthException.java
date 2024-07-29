package gift.common.exception;

import org.springframework.http.HttpStatus;

public class OAuthException extends RuntimeException {

    private final HttpStatus httpStatus;

    public OAuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
