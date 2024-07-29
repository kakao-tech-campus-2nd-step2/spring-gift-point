package gift.utils.error;

public class WishListNotFoundException extends RuntimeException {

    public WishListNotFoundException(String message) {
        super(message);
    }
}
