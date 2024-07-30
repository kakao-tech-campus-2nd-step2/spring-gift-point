package gift.exception;

public class NoSuchProductException extends RuntimeException {

    public NoSuchProductException() {
        super("해당 상품이 존재하지 않습니다.");
    }
}
