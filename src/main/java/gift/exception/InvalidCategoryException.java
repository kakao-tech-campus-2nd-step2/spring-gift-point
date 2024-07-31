package gift.exception;

import org.springframework.http.HttpStatus;

public class InvalidCategoryException extends RuntimeException {
	private HttpStatus status;

    public InvalidCategoryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
