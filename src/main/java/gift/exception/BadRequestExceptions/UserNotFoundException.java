package gift.exception.BadRequestExceptions;

public class UserNotFoundException extends BadRequestException {
    private UserNotFoundException() {super();}
    public UserNotFoundException(String message) {super(message);}
}
