package gift.member.exception;

import gift.common.exception.BusinessException;

public class MemberDeleteException extends BusinessException {

    public static BusinessException EXCEPTION = new MemberDeleteException();

    private MemberDeleteException() {
        super(MemberErrorCode.MEMBER_DELETE_FAILED);
    }
}
