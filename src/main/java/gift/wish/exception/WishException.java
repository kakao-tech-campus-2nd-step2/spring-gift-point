package gift.wish.exception;

import org.springframework.http.HttpStatus;

public class WishException extends RuntimeException {
    private final HttpStatus httpStatus;

    public WishException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
