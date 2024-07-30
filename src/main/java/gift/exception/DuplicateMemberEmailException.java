package gift.exception;

public class DuplicateMemberEmailException extends BusinessException {
    public DuplicateMemberEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
