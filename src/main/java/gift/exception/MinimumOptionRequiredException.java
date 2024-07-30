package gift.exception;

public class MinimumOptionRequiredException extends BusinessException {
    public  MinimumOptionRequiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
