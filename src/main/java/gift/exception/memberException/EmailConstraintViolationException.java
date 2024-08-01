package gift.exception.memberException;

public class EmailConstraintViolationException extends RuntimeException {
    public EmailConstraintViolationException(String email) {
        super(email+"은 제약 조건을 위배하였습니다.");
    }
}
