package gift.exception.customException;

public class DuplicateOptionNameException extends RuntimeException{
    public DuplicateOptionNameException(String message){
        super(message);
    }
}
