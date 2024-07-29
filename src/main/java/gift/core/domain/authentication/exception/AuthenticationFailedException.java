package gift.core.domain.authentication.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class AuthenticationFailedException extends APIException {
    public AuthenticationFailedException() {
        super(ErrorCode.AUTHENTICATION_FAILED);
    }
}
