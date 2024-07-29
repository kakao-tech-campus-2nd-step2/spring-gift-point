package gift.exception;

import java.util.List;
import org.springframework.validation.ObjectError;

public class InputException extends RuntimeException {

    List<ObjectError> errors;

    public InputException() {
        super();
    }

    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputException(Throwable cause) {
        super(cause);
    }

    protected InputException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InputException(List<ObjectError> allErrors) {
        super();
        errors = allErrors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }
}
