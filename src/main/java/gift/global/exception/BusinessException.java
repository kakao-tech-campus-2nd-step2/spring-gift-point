package gift.global.exception;

import org.springframework.http.HttpStatus;

/**
 * RuntimeException 을 상속받는 커스텀 에러 개발자가 직접 날리는 에러
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.status = errorCode.getHttpStatus();
    }

    public HttpStatus getStatus() {
        return status;
    }
}
