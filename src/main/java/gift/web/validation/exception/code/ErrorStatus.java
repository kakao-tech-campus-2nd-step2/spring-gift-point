package gift.web.validation.exception.code;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드<br>
 * code - HTTP 상태 코드 + 두 자리 숫자(내부 규칙 정의) 으로 정의된다.<br>
 */
public enum ErrorStatus {

    UNAUTHORIZED_INVALID_CREDENTIALS(-40100, Category.AUTHENTICATION, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_INVALID_TOKEN(-40101, Category.AUTHENTICATION, HttpStatus.UNAUTHORIZED),

    BAD_REQUEST(-40000, Category.COMMON, HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER(-40001, Category.COMMON, HttpStatus.BAD_REQUEST),
    KAKAO_APPROVAL_NEEDED(-40002, Category.POLICY, HttpStatus.BAD_REQUEST),
    SPECIAL_CHARACTER_NOT_ALLOWED(-40003, Category.POLICY, HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_FORMAT(-40004, Category.POLICY, HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD(-40005, Category.AUTHENTICATION, HttpStatus.BAD_REQUEST),
    INCORRECT_EMAIL(-40006, Category.AUTHENTICATION, HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS(-40007, Category.COMMON, HttpStatus.BAD_REQUEST),

    NOT_FOUND(-40400, Category.COMMON, HttpStatus.NOT_FOUND),

    INTERNAL_SERVER_ERROR(-50000, Category.COMMON, HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final int code;
    private final Category category;
    private final HttpStatus httpStatus;

    ErrorStatus(int code, Category category, HttpStatus httpStatus) {
        this.code = code;
        this.category = category;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public Category getCategory() {
        return category;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
