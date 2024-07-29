package gift.utils.error;

public class UserPasswordNotFoundException extends RuntimeException {

    public UserPasswordNotFoundException(String message) {
        super(message);
    }
}
