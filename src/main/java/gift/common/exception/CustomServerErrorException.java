package gift.common.exception;

public class CustomServerErrorException extends RuntimeException{

    public CustomServerErrorException(String message) {
        super(message);
    }
}
