package gift.core.domain.authentication.exception;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;

public class AuthenticationExpiredException extends APIException {
    public AuthenticationExpiredException() {
        super(ErrorCode.AUTHENTICATION_EXPIRED);
    }
}
