package gift.exception;

public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {
        super("Unauthenticated");
    }

    public UnauthenticatedException(String message) {
        super(message);
    }
}