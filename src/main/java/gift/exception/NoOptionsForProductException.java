package gift.exception;

public class NoOptionsForProductException extends RuntimeException {

    public NoOptionsForProductException() {
        super("상품에는 하나 이상의 옵션이 있어야 합니다.");
    }
}
