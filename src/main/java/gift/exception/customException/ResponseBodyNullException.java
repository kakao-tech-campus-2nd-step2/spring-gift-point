package gift.exception.customException;

public class ResponseBodyNullException extends RuntimeException{
    public ResponseBodyNullException (String message){
        super(message);
    }
}
