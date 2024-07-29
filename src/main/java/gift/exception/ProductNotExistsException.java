package gift.exception;

public class ProductNotExistsException extends NotFoundException {

    public ProductNotExistsException() {
        super("Product does not exist");
    }
}