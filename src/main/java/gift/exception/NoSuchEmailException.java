package gift.exception;

public class NoSuchEmailException extends RuntimeException {

    public NoSuchEmailException(String message) {
        super(message);
    }
}