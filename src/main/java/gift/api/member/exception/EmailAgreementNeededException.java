package gift.api.member.exception;

import gift.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class EmailAgreementNeededException extends GlobalException {

    public static final String MESSAGE = "Email needs agreement.";

    public EmailAgreementNeededException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
