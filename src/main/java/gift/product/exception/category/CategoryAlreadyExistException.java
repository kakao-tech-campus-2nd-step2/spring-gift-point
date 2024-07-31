package gift.product.exception.category;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistException extends CategoryException {

    private static final String MESSAGE = "Category를 [%s]는 이미 존재하는 카테고리입니다.";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private CategoryAlreadyExistException(String categoryName) {
        super(String.format(MESSAGE, categoryName), STATUS);
    }

    public static CategoryAlreadyExistException of(String categoryName) {
        return new CategoryAlreadyExistException(categoryName);
    }
}
