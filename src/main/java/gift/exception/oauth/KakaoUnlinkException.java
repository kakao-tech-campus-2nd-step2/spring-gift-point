package gift.exception.oauth;

import gift.exception.KakaoOAuthException;
import org.springframework.http.HttpStatus;

public class KakaoUnlinkException extends KakaoOAuthException {

    public KakaoUnlinkException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
