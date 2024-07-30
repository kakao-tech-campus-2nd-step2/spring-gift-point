package gift.exception;

public class AlreadyExistMemberException extends RuntimeException {

    public AlreadyExistMemberException() {
        super("이미 가입된 이메일입니다.");
    }
}
