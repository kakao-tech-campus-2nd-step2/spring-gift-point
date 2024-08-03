package gift.web.exception.invalidvalue;

public class InvalidValueException extends IllegalArgumentException{
    public InvalidValueException(String message) {
        super(message);
    }
}
