package gift.exception.oauth;

import gift.exception.KakaoOAuthException;
import org.springframework.http.HttpStatus;

public class KakaoTokenNotFoundException extends KakaoOAuthException {

    public KakaoTokenNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
