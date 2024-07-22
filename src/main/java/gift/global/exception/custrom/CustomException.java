package gift.global.exception.custrom;

import gift.global.exception.ErrorCode;

public abstract class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String details;

    protected CustomException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}
