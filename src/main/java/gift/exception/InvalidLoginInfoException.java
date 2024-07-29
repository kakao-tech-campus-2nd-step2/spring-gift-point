package gift.exception;

public class InvalidLoginInfoException extends RuntimeException {
    public InvalidLoginInfoException(String message) {
        super(message);
    }
}
