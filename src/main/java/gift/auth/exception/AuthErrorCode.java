package gift.auth.exception;

import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {
    INVALID_ACCESS_TOKEN("A001", HttpStatus.UNAUTHORIZED, "올바른 토큰이 아닙니다."),
    INVALID_REFRESH_TOKEN("A002", HttpStatus.UNAUTHORIZED, "올바른 리프레시 토큰이 아닙니다."),

    // 로그인 실패: 이메일 또는 비밀번호가 일치하지 않음
    LOGIN_FAILED("A003", HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
