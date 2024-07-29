package gift.exception.product;

public class NotFoundProductException extends RuntimeException {
    private static final String NOT_FOUND_PRODUCT_MESSAGE = "상품이 존재하지 않습니다.";

    public NotFoundProductException() {
        super(NOT_FOUND_PRODUCT_MESSAGE);
    }

    public NotFoundProductException(String message) {
        super(message);
    }

    public NotFoundProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundProductException(Throwable cause) {
        super(cause);
    }

    protected NotFoundProductException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
