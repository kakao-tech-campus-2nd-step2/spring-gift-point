package gift.global.exception;

import org.springframework.http.HttpStatus;

public class NoSuchEntityException extends GlobalException {

    private static final String MESSAGE_FORMAT = "No such %s exists.";

    public NoSuchEntityException(String element) {
        super(String.format(MESSAGE_FORMAT, element), HttpStatus.BAD_REQUEST);
    }
}
