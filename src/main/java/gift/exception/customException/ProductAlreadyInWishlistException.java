package gift.exception.customException;

public class ProductAlreadyInWishlistException extends RuntimeException {
    public ProductAlreadyInWishlistException(String message) {
        super(message);
    }
}
