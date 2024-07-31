package gift.core.exception.product;

public class DuplicateProductIdException extends RuntimeException {
	public DuplicateProductIdException(String name) {
		super("Product with ID " + name + " already exists");
	}
}