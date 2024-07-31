package gift.exception.member;

import static gift.exception.ErrorCode.PASSWORD_MISMATCH;

import gift.exception.CustomException;

public class PasswordMismatchException extends CustomException {

    public PasswordMismatchException() {
        super(PASSWORD_MISMATCH);
    }
}
