package gift.exception.category;

public class NotFoundCategoryException extends RuntimeException {

    private static final String NOT_FOUND_CATEGORY_MESSAGE = "해당 카테고리가 존재하지 않습니다.";

    public NotFoundCategoryException() {
        super(NOT_FOUND_CATEGORY_MESSAGE);
    }

    public NotFoundCategoryException(String message) {
        super(message);
    }

    public NotFoundCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCategoryException(Throwable cause) {
        super(cause);
    }

    protected NotFoundCategoryException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
