package gift.exception;

public class CategoryNameDuplicateException extends RuntimeException {
    public CategoryNameDuplicateException(String duplicatedName) {
        super(duplicatedName + " already in use");
    }
}
