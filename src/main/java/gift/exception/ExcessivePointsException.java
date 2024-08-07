package gift.exception;

public class ExcessivePointsException extends BusinessException {
    public ExcessivePointsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
