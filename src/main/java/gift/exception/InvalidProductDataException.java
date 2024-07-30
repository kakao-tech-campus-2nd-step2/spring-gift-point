package gift.exception;

public class InvalidProductDataException extends RuntimeException {
    public InvalidProductDataException(String message, Throwable cause) {
        super(message, cause);
    }
}