package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WishAlreadyExistException extends RuntimeException {
    public WishAlreadyExistException(String message) {
        super(message);
    }
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
