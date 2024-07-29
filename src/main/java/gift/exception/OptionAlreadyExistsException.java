package gift.exception;

public class OptionAlreadyExistsException extends ConflictException {

    public OptionAlreadyExistsException() {
        super("Option already exists");
    }
}
