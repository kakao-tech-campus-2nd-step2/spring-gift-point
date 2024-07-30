package gift.Exception.wish;

public class WishNotFoundException extends RuntimeException {

    public WishNotFoundException() {
        super();
    }

    public WishNotFoundException(String message) {
        super(message);
    }

    public WishNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WishNotFoundException(Throwable cause) {
        super(cause);
    }

    protected WishNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
