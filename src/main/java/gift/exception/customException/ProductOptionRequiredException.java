package gift.exception.customException;

public class ProductOptionRequiredException extends RuntimeException{
    public ProductOptionRequiredException(String message){
        super(message);
    }
}
