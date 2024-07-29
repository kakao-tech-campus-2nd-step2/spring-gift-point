package gift.member.exception;

import gift.common.exception.BusinessException;

public class MemberCreateException extends BusinessException {

    public static BusinessException EXCEPTION = new MemberCreateException();

    private MemberCreateException() {
        super(MemberErrorCode.MEMBER_CREATE_FAILED);
    }
}
