package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WishNotFoundException extends RuntimeException {
    public WishNotFoundException(String message) {
        super(message);
    }
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
