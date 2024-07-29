package gift.exception;

public class AuthorizationCodeException extends ConflictException {

    public AuthorizationCodeException() {
        super("Authorization Code Exception occurred");
    }

    public AuthorizationCodeException(String message) {
        super(message);
    }
}
