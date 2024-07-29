package gift.exception.oauth2;

public class OAuth2Exception extends RuntimeException {

    public OAuth2Exception() {
        super();
    }

    public OAuth2Exception(String error, String errorDescription) {
        super(String.format("Error: %s, Description: %s", error, errorDescription));
    }

    public OAuth2Exception(String message) {
        super(message);
    }

    public OAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuth2Exception(Throwable cause) {
        super(cause);
    }

    protected OAuth2Exception(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
