package gift.exception.customException;


public class ToMuchPointException extends RuntimeException{

    public ToMuchPointException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
