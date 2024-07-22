package gift.exception.member;

import gift.exception.MemberException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends MemberException {

    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
