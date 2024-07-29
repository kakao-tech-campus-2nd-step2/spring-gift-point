package gift.exception.oauth;

import gift.exception.KakaoOAuthException;
import org.springframework.http.HttpStatus;

public class KakaoScopeException extends KakaoOAuthException {

    public KakaoScopeException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
