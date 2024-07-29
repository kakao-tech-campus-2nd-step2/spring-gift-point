package gift.exception.customException;

public class DuplicateCategoryNameException extends RuntimeException{
    public  DuplicateCategoryNameException(String message){
        super(message);
    }
}
