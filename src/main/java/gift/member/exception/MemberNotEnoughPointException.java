package gift.member.exception;

import gift.common.exception.BusinessException;

public class MemberNotEnoughPointException extends BusinessException {

    public static BusinessException EXCEPTION = new MemberNotEnoughPointException();

    public MemberNotEnoughPointException() {
        super(MemberErrorCode.MEMBER_NOT_ENOUGH_POINT);
    }
}
