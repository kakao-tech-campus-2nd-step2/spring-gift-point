package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAuthCodeException extends RuntimeException {
    public InvalidAuthCodeException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
