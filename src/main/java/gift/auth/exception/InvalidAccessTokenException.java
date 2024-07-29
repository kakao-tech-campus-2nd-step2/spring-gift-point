package gift.auth.exception;

import gift.common.exception.BusinessException;

public class InvalidAccessTokenException extends BusinessException {

    public static BusinessException EXCEPTION = new InvalidAccessTokenException();

    private InvalidAccessTokenException() {
        super(AuthErrorCode.INVALID_ACCESS_TOKEN);
    }
}
