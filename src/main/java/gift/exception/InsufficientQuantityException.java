package gift.exception;

public class InsufficientQuantityException extends BusinessException {
    public InsufficientQuantityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
