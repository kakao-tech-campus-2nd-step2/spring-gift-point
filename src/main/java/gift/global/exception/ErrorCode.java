package gift.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DB_NOT_FOUND(HttpStatus.NOT_FOUND, "요청에 대한 값이 DB에 존재하지 않습니다."),
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 처리 중 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상하지 못한 예외가 발생했습니다."),
    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 올바르지 않습니다."),
    LOGIN_ERROR(HttpStatus.FORBIDDEN, "잘못된 로그인 관련 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
