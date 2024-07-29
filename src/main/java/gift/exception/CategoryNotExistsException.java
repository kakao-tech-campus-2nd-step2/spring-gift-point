package gift.exception;

public class CategoryNotExistsException extends NotFoundException{

    public CategoryNotExistsException() {
        super("Category not exists");
    }
}