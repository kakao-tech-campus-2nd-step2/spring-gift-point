package gift.exception.member;

import static gift.exception.ErrorCode.DUPLICATED_EMAIL;

import gift.exception.CustomException;

public class DuplicatedEmailException extends CustomException {
    public DuplicatedEmailException() {
        super(DUPLICATED_EMAIL);
    }
}
