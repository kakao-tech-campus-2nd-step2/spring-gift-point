package gift.Exception;

public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException(String message){
        super(message);
    }
}
