package gift.member.exception;

import gift.common.exception.BusinessException;

public class MemberNotFoundByIdException extends BusinessException {

    public static BusinessException EXCEPTION = new MemberNotFoundByIdException();

    private MemberNotFoundByIdException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND_BY_ID);
    }
}
