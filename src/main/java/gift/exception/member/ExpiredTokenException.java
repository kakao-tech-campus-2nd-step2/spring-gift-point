package gift.exception.member;

import gift.exception.MemberException;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends MemberException {

    public ExpiredTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
