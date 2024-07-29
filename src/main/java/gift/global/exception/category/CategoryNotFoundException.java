package gift.global.exception.category;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("id 가 " + id + " 인 카테고리를 찾을 수 없습니다.");
    }
}
