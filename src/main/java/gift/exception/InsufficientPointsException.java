package gift.exception;

public class InsufficientPointsException extends BusinessException {
    public InsufficientPointsException(ErrorCode errorCode) {
        super(errorCode);
    }
}