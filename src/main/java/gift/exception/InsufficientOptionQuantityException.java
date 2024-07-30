package gift.exception;

public class InsufficientOptionQuantityException extends RuntimeException {
    public InsufficientOptionQuantityException(int quantity) {
        super("Existing quantity is less than " + quantity);
    }
}
