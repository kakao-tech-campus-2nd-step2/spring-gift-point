package gift.global.exception.custrom;

import gift.global.exception.ErrorCode;

public class LoginException extends CustomException {
    public LoginException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }
}
