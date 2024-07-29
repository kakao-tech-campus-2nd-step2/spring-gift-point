package gift.exception.wishlist;

public class DuplicateWishException extends WishException {

    private static final String DUPLICATE_WISH_MESSAGE = "해당 상품이 이미 위시 리스트에 존재합니다.";

    public DuplicateWishException() {
        super(DUPLICATE_WISH_MESSAGE);
    }

    public DuplicateWishException(String message) {
        super(message);
    }

    public DuplicateWishException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateWishException(Throwable cause) {
        super(cause);
    }

    protected DuplicateWishException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
