package gift.exception;

public class NoSuchMemberException extends RuntimeException {

    public NoSuchMemberException() {
        super("가입되지 않은 이메일입니다.");
    }
}
