package gift.core.exception.product;

public class DuplicateOptionNameException extends RuntimeException{
	public DuplicateOptionNameException(String name) {
		super("Duplicate option name: " + name);
	}
}