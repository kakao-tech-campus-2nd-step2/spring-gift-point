package gift.exception;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private Object id;

    public CustomException(ErrorCode errorCode, Object id) {
        super(errorCode.getFormattedMessage(id));
        this.errorCode = errorCode;
        this.id = id;
    }

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getId() {
        return id;
    }
}
