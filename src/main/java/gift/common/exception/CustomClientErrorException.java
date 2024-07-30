package gift.common.exception;

public class CustomClientErrorException extends RuntimeException{

    public CustomClientErrorException(String message) {
        super(message);
    }
}
