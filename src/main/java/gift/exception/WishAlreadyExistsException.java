package gift.exception;

public class WishAlreadyExistsException extends ConflictException {

    public WishAlreadyExistsException() {
        super("Wish already exists");
    }
}
