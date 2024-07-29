package gift.exception.option;

import gift.exception.OptionException;
import org.springframework.http.HttpStatus;

public class OptionNotFoundException extends OptionException {

    public OptionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
