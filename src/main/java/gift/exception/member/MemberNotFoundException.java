package gift.exception.member;

import static gift.exception.ErrorCode.MEMBER_NOT_FOUND;

import gift.exception.CustomException;

public class MemberNotFoundException extends CustomException {

    public MemberNotFoundException() {
        super(MEMBER_NOT_FOUND);
    }
}
