package gift.exception;

public class DuplicateOptionNameException extends BusinessException{
    public DuplicateOptionNameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
