package gift.exception.wishlist;

public class NotFoundWishException extends WishException{

    private static final String NOT_FOUND_WISH_MESSAGE = "해당 상품이 위시 리스트에 존재하지 않습니다.";

    public NotFoundWishException() {
        super(NOT_FOUND_WISH_MESSAGE);
    }

    public NotFoundWishException(String message) {
        super(message);
    }

    public NotFoundWishException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundWishException(Throwable cause) {
        super(cause);
    }

    protected NotFoundWishException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
