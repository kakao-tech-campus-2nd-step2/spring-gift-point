package gift.member.exception;

import gift.common.exception.BusinessException;

public class MemberUpdateException extends BusinessException {

    public static BusinessException EXCEPTION = new MemberUpdateException();

    private MemberUpdateException() {
        super(MemberErrorCode.MEMBER_UPDATE_FAILED);
    }
}
