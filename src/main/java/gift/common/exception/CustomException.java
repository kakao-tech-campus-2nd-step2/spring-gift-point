package gift.common.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException{
    private final HttpStatus status;
    private final String title;

    public CustomException(String message, HttpStatus status, String title) {
        super(message);
        this.status = status;
        this.title = title;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
