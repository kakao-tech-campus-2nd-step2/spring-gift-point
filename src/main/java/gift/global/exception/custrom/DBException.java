package gift.global.exception.custrom;

import gift.global.exception.ErrorCode;

public class DBException extends CustomException {
    public DBException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }
}
