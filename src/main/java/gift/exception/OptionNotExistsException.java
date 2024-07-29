package gift.exception;

public class OptionNotExistsException extends NotFoundException{

    public OptionNotExistsException() {
        super("Option not exists");
    }
}
