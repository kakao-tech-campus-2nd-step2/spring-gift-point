package gift.exception;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}