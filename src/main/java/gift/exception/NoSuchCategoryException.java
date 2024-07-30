package gift.exception;

public class NoSuchCategoryException extends RuntimeException {

    public NoSuchCategoryException() {
        super("해당 카테고리가 존재하지 않습니다");
    }
}
