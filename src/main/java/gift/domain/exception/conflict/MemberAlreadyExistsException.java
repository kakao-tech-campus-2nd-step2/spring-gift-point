package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class MemberAlreadyExistsException extends ConflictException {

    public MemberAlreadyExistsException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTS);
    }
}
