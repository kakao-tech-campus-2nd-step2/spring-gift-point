package gift.exception;

public class GiftException extends RuntimeException {

    private ErrorCode errorCode;

    public GiftException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorMessage() {
        return errorCode;
    }

}
