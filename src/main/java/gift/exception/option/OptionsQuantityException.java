package gift.exception.option;

public class OptionsQuantityException extends RuntimeException {

    private static final String OPTIONS_QUANTITY_MESSAGE = "재고가 부족합니다.";

    public OptionsQuantityException() {
        super(OPTIONS_QUANTITY_MESSAGE);
    }

    public OptionsQuantityException(String message) {
        super(message);
    }

    public OptionsQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionsQuantityException(Throwable cause) {
        super(cause);
    }

    protected OptionsQuantityException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
