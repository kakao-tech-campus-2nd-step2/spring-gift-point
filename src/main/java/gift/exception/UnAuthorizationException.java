package gift.exception;

public class UnAuthorizationException extends BusinessException {
    public UnAuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}