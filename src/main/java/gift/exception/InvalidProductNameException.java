package gift.exception;

public class InvalidProductNameException extends RuntimeException {
    public InvalidProductNameException(String message) {
        super(message);
    }
}