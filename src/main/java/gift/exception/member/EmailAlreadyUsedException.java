package gift.exception.member;

import gift.exception.MemberException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyUsedException extends MemberException {

    public EmailAlreadyUsedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
