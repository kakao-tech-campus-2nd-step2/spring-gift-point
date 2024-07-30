package gift.exception;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
