package gift.api.member.exception;

import gift.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class RegisterNeededException extends GlobalException {

    private static final String MESSAGE = "Need to register.";

    public RegisterNeededException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
