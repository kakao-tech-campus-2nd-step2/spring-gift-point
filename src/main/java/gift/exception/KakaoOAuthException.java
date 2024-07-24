package gift.exception;

import org.springframework.http.HttpStatus;

public class KakaoOAuthException extends ApplicationException {

    public KakaoOAuthException(String message, HttpStatus status) {
        super(message, status);
    }
}
