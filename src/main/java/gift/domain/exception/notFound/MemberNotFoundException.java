package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
