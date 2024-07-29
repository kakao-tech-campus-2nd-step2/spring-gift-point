package gift.exception;

public class ProductHasNotOptionException extends ConflictException {

    public ProductHasNotOptionException() {
        super("Product has no option");
    }
}
