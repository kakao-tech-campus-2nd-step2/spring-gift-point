package gift.exception.customException;

public class OptionAlreadyInWishlistException extends RuntimeException {
    public OptionAlreadyInWishlistException(String message) {
        super(message);
    }
}
