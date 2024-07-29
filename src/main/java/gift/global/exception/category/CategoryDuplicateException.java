package gift.global.exception.category;

public class CategoryDuplicateException extends RuntimeException {

    public CategoryDuplicateException(String categoryName) {
        super(categoryName + " 이름을 가진 카테고리가 이미 존재합니다.");
    }
}
