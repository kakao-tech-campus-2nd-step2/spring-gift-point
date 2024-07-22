package gift.member.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class DuplicateNicknameException extends BusinessException {
    public DuplicateNicknameException() {
        super(ErrorCode.DUPLICATE_NICKNAME_ERROR);
    }
}
