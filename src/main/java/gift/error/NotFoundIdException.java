package gift.error;

public class NotFoundIdException extends RuntimeException{
    public NotFoundIdException(String message) {
        super(message);
    }
}
