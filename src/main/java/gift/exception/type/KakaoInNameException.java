package gift.exception.type;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class KakaoInNameException extends ApplicationException {

    public KakaoInNameException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }
}
