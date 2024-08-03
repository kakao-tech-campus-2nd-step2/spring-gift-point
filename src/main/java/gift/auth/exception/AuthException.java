package gift.auth.exception;

import org.springframework.http.HttpStatus;

/**
 * 인증 예외 클래스
 */
public class AuthException extends RuntimeException {

    private final HttpStatus status;

    /**
     * AuthException 생성자
     *
     * @param message 예외 메시지
     * @param status  HTTP 상태
     */
    public AuthException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * 상태 반환 메서드
     *
     * @return HTTP 상태
     */
    public HttpStatus getStatus() {
        return status;
    }
}
