package gift.exception.BadRequestExceptions;

public class BlankContentException extends BadRequestException {

    private BlankContentException() {
        super();
    }

    public BlankContentException(String message) {
        super(message);
    }
}
