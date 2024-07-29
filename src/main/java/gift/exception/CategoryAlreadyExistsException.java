package gift.exception;

public class CategoryAlreadyExistsException extends ConflictException{

    public CategoryAlreadyExistsException() {
        super("Category already exists");
    }
}
