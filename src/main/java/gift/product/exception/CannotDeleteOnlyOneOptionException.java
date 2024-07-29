package gift.product.exception;

public class CannotDeleteOnlyOneOptionException extends RuntimeException {

    public CannotDeleteOnlyOneOptionException() {

    }

    public CannotDeleteOnlyOneOptionException(String message) {
        super(message);
    }

}
