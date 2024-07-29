package gift.exception.option;

public class DuplicateOptionsException extends RuntimeException {

    private static final String DUPLICATE_OPTIONS_MESSAGE = "중복된 옵션이 이미 존재합니다.";

    public DuplicateOptionsException() {
        super(DUPLICATE_OPTIONS_MESSAGE);
    }

    public DuplicateOptionsException(String message) {
        super(message);
    }

    public DuplicateOptionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateOptionsException(Throwable cause) {
        super(cause);
    }

    protected DuplicateOptionsException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
