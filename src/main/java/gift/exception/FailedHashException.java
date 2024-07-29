package gift.exception;

public class FailedHashException extends RuntimeException {

    public FailedHashException() {
        super("hash failed");
    }
}