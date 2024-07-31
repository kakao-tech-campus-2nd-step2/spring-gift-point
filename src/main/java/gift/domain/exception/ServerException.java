package gift.domain.exception;

public abstract class ServerException extends RuntimeException {

    private final ErrorCode errorCode;

    public ServerException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
