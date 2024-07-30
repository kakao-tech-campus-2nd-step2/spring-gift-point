package gift.global.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenMemberException extends GlobalException {

    private static final String MESSAGE = "Forbidden member.";

    public ForbiddenMemberException() {
        super(MESSAGE, HttpStatus.FORBIDDEN);
    }
}
