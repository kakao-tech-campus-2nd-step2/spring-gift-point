package gift.exception.order;

public class NotFoundOrderException extends RuntimeException {

    private static final String NOT_FOUND_ORDER_MESSAGE = "해당 주문을 조회할 수 없습니다.";

    public NotFoundOrderException() {
        super(NOT_FOUND_ORDER_MESSAGE);
    }

    public NotFoundOrderException(String message) {
        super(message);
    }

    public NotFoundOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundOrderException(Throwable cause) {
        super(cause);
    }

    protected NotFoundOrderException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
