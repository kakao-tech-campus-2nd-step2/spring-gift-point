package gift.exception.customException;

public class DuplicateMemberEmailException extends RuntimeException {
    public DuplicateMemberEmailException(String message){
        super(message);
    }
}
