package gift.domain.exception.forbidden;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;

public class MemberIncorrectLoginInfoException extends ServerException {

    public MemberIncorrectLoginInfoException() {
        super("Incorrect your email or password. Try again.", ErrorCode.MEMBER_INCORRECT_LOGIN_INFO);
    }
}
