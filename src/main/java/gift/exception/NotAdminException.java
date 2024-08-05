package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAdminException extends RuntimeException {
    public NotAdminException(String message) {
        super(message);
    }
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }

}
