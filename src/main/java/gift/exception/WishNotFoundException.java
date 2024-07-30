package gift.exception;

public class WishNotFoundException extends RuntimeException {

    public WishNotFoundException() {
        super("Wish not found");
    }

}
