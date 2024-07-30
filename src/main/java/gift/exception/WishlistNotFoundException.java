package gift.exception;

public class WishlistNotFoundException extends BusinessException {
    public WishlistNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}