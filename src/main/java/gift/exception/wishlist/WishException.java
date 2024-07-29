package gift.exception.wishlist;

public class WishException extends RuntimeException {

    public WishException() {
    }

    public WishException(String message) {
        super(message);
    }

    public WishException(String message, Throwable cause) {
        super(message, cause);
    }

    public WishException(Throwable cause) {
        super(cause);
    }

    public WishException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
