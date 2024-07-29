package gift.exception.BadRequestExceptions;

public class InvalidIdException extends BadRequestException {
    private InvalidIdException() {
        super();
    }

    public InvalidIdException(String message) {
        super(message);
    }
}
