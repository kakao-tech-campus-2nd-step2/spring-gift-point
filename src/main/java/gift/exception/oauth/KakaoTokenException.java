package gift.exception.oauth;

import gift.exception.KakaoOAuthException;
import org.springframework.http.HttpStatus;

public class KakaoTokenException extends KakaoOAuthException {

    public KakaoTokenException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
