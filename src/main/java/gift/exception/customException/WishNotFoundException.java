package gift.exception.customException;

public class WishNotFoundException extends RuntimeException{
    public WishNotFoundException(String message){
        super(message);
    }
}
