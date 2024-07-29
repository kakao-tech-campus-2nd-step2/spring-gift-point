package gift.exception;

import org.springframework.http.HttpStatus;

public class MemberException extends ApplicationException {

    public MemberException(String message, HttpStatus status) {
        super(message, status);
    }
}
