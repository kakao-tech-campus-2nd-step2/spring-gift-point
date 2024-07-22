package gift.exception;

import org.springframework.http.HttpStatus;

public class OptionException extends ApplicationException {

    public OptionException(String message, HttpStatus status) {
        super(message, status);
    }
}
