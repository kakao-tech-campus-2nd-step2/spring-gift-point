package gift.core.domain.authentication.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class AuthenticationRequiredException extends APIException {
    public AuthenticationRequiredException() {
        super(ErrorCode.AUTHENTICATION_REQUIRED);
    }
}
