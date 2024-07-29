package gift.exception.member;

public class DuplicateEmailException extends RuntimeException {

    private static final String DUPLICATE_EMAIL_MESSAGE = "중복된 이메일의 회원이 이미 존재합니다.";

    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL_MESSAGE);
    }

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEmailException(Throwable cause) {
        super(cause);
    }

    protected DuplicateEmailException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
