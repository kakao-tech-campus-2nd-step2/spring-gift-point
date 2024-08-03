package gift.exception;

public class OptionNotMatchProductException extends BusinessException {
    public OptionNotMatchProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}