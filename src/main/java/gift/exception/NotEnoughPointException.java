package gift.exception;

public class NotEnoughPointException extends ConflictException {
    public NotEnoughPointException() {
        super("Not enough point");
    }
}
