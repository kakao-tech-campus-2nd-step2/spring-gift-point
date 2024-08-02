package gift.exception.member;

public class InvalidPointException extends RuntimeException {

    private static final String INVALID_POINT_MESSAGE = "보유한 포인트를 초과하여 사용할 수 없습니다.";

    public InvalidPointException() {
        super();
    }

    public InvalidPointException(String message) {
        super(message);
    }

    public InvalidPointException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPointException(Throwable cause) {
        super(cause);
    }

    protected InvalidPointException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
