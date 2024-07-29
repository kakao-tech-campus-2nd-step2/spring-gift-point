package gift.exception.customException;

public class OptionNotFoundException extends RuntimeException{
    public OptionNotFoundException(String message){
        super(message);
    }
}
