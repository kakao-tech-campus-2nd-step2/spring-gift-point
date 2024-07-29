package gift.exception.customException;

public class MissingAuthorizationCodeException extends RuntimeException{
    public MissingAuthorizationCodeException(String message){
        super(message);
    }
}
