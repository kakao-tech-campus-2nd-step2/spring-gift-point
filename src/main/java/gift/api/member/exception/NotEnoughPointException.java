package gift.api.member.exception;

import gift.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class NotEnoughPointException extends GlobalException {

    public static final String MESSAGE = "Not enough points left.";

    public NotEnoughPointException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
