package gift.exception.memberException;

public class DuplicatedEmailException extends RuntimeException{
    public DuplicatedEmailException(String message){
        super(message);
    }
}
