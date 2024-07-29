package gift.exception.oauth;

import gift.exception.KakaoOAuthException;
import org.springframework.http.HttpStatus;

public class KakaoUserInfoException extends KakaoOAuthException {

    public KakaoUserInfoException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
