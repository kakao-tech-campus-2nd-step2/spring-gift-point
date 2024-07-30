package gift.exception;

public class DuplicateCategoryNameException extends BusinessException {
    public DuplicateCategoryNameException(ErrorCode errorCode) {
        super(errorCode);
    }
}