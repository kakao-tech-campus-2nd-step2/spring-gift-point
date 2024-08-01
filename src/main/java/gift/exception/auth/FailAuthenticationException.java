package gift.exception.auth;

public class FailAuthenticationException extends RuntimeException {

    public FailAuthenticationException() {
        super();
    }

    public FailAuthenticationException(String message) {
        super(message);
    }

    public FailAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailAuthenticationException(Throwable cause) {
        super(cause);
    }

    protected FailAuthenticationException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
