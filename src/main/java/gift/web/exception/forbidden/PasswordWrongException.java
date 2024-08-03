package gift.web.exception.forbidden;

public class PasswordWrongException extends ForbiddenException {
    public PasswordWrongException() {
        super("비밀번호가 틀렸슴다!");
    }
}
