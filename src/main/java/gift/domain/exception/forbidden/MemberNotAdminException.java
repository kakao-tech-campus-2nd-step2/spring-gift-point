package gift.domain.exception.forbidden;

import gift.domain.exception.ErrorCode;

public class MemberNotAdminException extends ForbiddenException {

    public MemberNotAdminException() {
        super(ErrorCode.MEMBER_NOT_ADMIN);
    }
}
