package gift.exception;

public class WishNotExistsException extends NotFoundException {

    public WishNotExistsException() {
        super("wish not exists");
    }
}
