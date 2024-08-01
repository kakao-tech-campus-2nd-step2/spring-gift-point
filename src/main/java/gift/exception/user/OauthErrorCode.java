package gift.exception.user;

import gift.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum OauthErrorCode implements BaseErrorCode {
    FAILED_TO_SEND_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "메시지 전송에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    OauthErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
