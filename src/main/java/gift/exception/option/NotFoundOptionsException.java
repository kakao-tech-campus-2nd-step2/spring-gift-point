package gift.exception.option;

public class NotFoundOptionsException extends RuntimeException{

    private static final String NOT_FOUND_OPTIONS_MESSAGE = "해당 상품 옵션이 존재하지 않습니다.";

    public NotFoundOptionsException() {
        super(NOT_FOUND_OPTIONS_MESSAGE);
    }

    public NotFoundOptionsException(String message) {
        super(message);
    }

    public NotFoundOptionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundOptionsException(Throwable cause) {
        super(cause);
    }

    protected NotFoundOptionsException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
