package gift.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class InvalidKakaoTokenException extends ResponseStatusException {

    public InvalidKakaoTokenException(HttpStatusCode httpStatusCode, String reason) {
        super(httpStatusCode, reason);
    }
}
