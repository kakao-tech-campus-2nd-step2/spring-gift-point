package gift.exception;

public class InsufficientQuantityException extends RuntimeException {

    public InsufficientQuantityException(int quantity) {
        super("해당 옵션의 수량이 부족합니다. (남은 수량: %d개)".formatted(quantity));
    }
}
