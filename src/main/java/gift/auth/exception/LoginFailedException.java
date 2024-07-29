package gift.auth.exception;

import gift.common.exception.BusinessException;

public class LoginFailedException extends BusinessException {

    public static BusinessException EXCEPTION = new LoginFailedException();

    private LoginFailedException() {
        super(AuthErrorCode.LOGIN_FAILED);
    }
}
