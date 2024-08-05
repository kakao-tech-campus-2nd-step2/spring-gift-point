package gift.exception;

public class InsufficientPointException extends RuntimeException {
    public InsufficientPointException() {
        super("잔여 포인트가 부족합니다.");
    }
}
