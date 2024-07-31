package gift.product.exception.category;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends CategoryException {
    private static final String MESSAGE = "Category를 [%s]를 찾을 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private CategoryNotFoundException(Long categoryId) {
        super(String.format(MESSAGE, categoryId), STATUS);
    }

    public static CategoryNotFoundException of(Long categoryId) {
        return new CategoryNotFoundException(categoryId);
    }
}
