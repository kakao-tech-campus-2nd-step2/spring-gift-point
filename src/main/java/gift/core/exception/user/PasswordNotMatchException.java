package gift.core.exception.user;

public class PasswordNotMatchException extends RuntimeException{
	public PasswordNotMatchException() {
		super("Password not match");
	}
}
