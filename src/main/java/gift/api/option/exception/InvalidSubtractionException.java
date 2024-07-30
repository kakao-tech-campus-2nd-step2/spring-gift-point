package gift.api.option.exception;

import gift.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class InvalidSubtractionException extends GlobalException {

    public static final String MESSAGE = "Option quantity must be greater than or equal to 0.";

    public InvalidSubtractionException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
