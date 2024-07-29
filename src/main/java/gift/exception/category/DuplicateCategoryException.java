package gift.exception.category;

public class DuplicateCategoryException extends RuntimeException {

    private static final String DUPLICATE_CATEGORY_MESSAGE = "중복된 카테고리가 이미 존재합니다.";

    public DuplicateCategoryException() {
        super(DUPLICATE_CATEGORY_MESSAGE);
    }

    public DuplicateCategoryException(String message) {
        super(message);
    }

    public DuplicateCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCategoryException(Throwable cause) {
        super(cause);
    }

    protected DuplicateCategoryException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
