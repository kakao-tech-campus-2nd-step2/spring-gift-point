package gift.exception.member;

import static gift.exception.ErrorCode.INVALID_ACCOUNT;

import gift.exception.CustomException;

public class InvalidAccountException extends CustomException {

    public InvalidAccountException() {
        super(INVALID_ACCOUNT);
    }
}