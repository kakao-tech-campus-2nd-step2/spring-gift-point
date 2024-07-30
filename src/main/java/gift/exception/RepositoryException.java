package gift.exception;

public class RepositoryException extends RuntimeException {

    private final ErrorCode errorCode;

    public RepositoryException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatMessage(args));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
