package gift.exception;

public class PasswordNotMatchedException extends UnauthenticatedException {

    public PasswordNotMatchedException() {
        super("Password is wrong");
    }
}
