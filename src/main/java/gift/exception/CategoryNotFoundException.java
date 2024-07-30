package gift.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long categoryId) {
        super("CategoryId: " + categoryId + " not found");
    }
}
