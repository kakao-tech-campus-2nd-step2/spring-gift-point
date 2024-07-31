package gift.core.exception.product;

public class CategoryNotFoundException extends RuntimeException{
	public CategoryNotFoundException(String name) {
		super("Category " + name + " is not found");
	}
}
