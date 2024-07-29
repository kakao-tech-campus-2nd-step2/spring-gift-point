package gift.exception.BadRequestExceptions;

public class NoSuchProductIdException extends BadRequestException {
    private NoSuchProductIdException(){
        super();
    }
    public NoSuchProductIdException(String message){
        super(message);
    }

}
