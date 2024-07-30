package gift.exception;

public class NoOptionsForProductException extends RuntimeException {

    public NoOptionsForProductException() {
        super("상품에는 최소 하나의 옵션이 있어야 합니다.");
    }
}
