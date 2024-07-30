package gift.exception.memberException;

public class MemberEmailNotFoundException extends RuntimeException {
    public MemberEmailNotFoundException(String message) {
        super(message);
    }
}
