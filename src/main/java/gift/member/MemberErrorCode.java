package gift.member;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode {
    FAILURE_LOGIN(HttpStatus.FORBIDDEN, "아이디나 비밀번호가 틀렸습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(HttpStatus httpStatus, String message) {
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
