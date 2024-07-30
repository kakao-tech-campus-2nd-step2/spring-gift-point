package gift.exception;

public class OptionNotFoundException extends BusinessException {
    public OptionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}