package gift.api.option.exception;

import gift.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class InvalidNameException extends GlobalException {

    public static final String MESSAGE = "Name already exists.";

    public InvalidNameException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
