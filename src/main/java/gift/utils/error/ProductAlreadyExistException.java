package gift.utils.error;

public class ProductAlreadyExistException extends RuntimeException {

    public ProductAlreadyExistException(String message) {
        super(message);
    }
}
