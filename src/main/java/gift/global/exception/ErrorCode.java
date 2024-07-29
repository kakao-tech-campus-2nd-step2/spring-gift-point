package gift.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST),         // 400
    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED),   // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),   // 401
    FORBIDDEN(HttpStatus.FORBIDDEN),             // 403
    NOT_FOUND(HttpStatus.NOT_FOUND),             // 404
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);   // 500


    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
