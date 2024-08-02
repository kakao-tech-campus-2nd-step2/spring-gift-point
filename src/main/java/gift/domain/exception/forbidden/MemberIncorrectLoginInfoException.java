package gift.domain.exception.forbidden;

import gift.domain.exception.ErrorCode;

public class MemberIncorrectLoginInfoException extends ForbiddenException {

    public MemberIncorrectLoginInfoException() {
        super(ErrorCode.MEMBER_INCORRECT_LOGIN_INFO);
    }
}
