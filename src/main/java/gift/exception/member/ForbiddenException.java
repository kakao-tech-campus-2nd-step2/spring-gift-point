package gift.exception.member;

import gift.exception.MemberException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends MemberException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
