package gift.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class KakaoException extends ResponseStatusException {
    public KakaoException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
