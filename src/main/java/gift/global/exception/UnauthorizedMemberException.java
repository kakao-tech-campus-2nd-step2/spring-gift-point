package gift.global.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedMemberException extends GlobalException {

    private static final String MESSAGE = "Unauthorized member.";

    public UnauthorizedMemberException() {
        super(MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
