package gift.member.exception;

public class InsufficientPointException extends RuntimeException {

    public InsufficientPointException(int point) {
        super("포인트가 부족합니다. (남은 포인트: %d)".formatted(point));
    }
}
