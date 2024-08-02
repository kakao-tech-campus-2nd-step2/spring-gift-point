package gift.exception.oauth2;

public class OAuth2TokenException extends RuntimeException {

    public OAuth2TokenException() {
        super();
    }

    public OAuth2TokenException(String message) {
        super(message);
    }

    public OAuth2TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuth2TokenException(Throwable cause) {
        super(cause);
    }

    protected OAuth2TokenException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
