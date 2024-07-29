package gift.exception.customException;

public class CannotDeleteLastOptionException extends RuntimeException{
    public CannotDeleteLastOptionException (String message){
        super(message);
    }
}
