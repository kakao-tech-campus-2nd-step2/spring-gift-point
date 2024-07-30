package gift.member.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND_ERROR);
    }
}
