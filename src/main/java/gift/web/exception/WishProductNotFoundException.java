package gift.web.exception;

public class WishProductNotFoundException extends RuntimeException{

    public WishProductNotFoundException(String message) {
        super(message);
    }
}
