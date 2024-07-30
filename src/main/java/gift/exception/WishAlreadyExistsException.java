package gift.exception;

public class WishAlreadyExistsException extends RuntimeException {

    public WishAlreadyExistsException(Long productId) {
        super("ProductId: " + productId + " already exist in your wishlist");
    }
}
