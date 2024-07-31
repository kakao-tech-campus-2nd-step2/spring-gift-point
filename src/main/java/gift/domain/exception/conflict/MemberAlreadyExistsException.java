package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class MemberAlreadyExistsException extends ConflictException {

    public MemberAlreadyExistsException() {
        super("Your email already registered. Retry with other one.", ErrorCode.MEMBER_ALREADY_EXISTS);
    }
}
