package gift.member.exception;

import gift.common.exception.BusinessException;

public class MemberAlreadyExistsByEmailException extends BusinessException {

    public static BusinessException EXCEPTION = new MemberAlreadyExistsByEmailException();

    private MemberAlreadyExistsByEmailException() {
        super(MemberErrorCode.MEMBER_ALREADY_EXISTS_BY_EMAIL);
    }
}
