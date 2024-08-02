package gift.exception.auth;

public class FailAuthorizationException extends RuntimeException {

    public FailAuthorizationException() {
        super();
    }

    public FailAuthorizationException(String message) {
        super(message);
    }

    public FailAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailAuthorizationException(Throwable cause) {
        super(cause);
    }

    protected FailAuthorizationException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
