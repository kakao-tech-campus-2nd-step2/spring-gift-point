package gift.exception;

public class ProductAlreadyExistsException extends ConflictException {

    public ProductAlreadyExistsException() {
        super("Product already exists");
    }
}
