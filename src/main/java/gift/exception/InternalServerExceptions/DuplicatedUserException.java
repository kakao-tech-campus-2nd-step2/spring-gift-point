package gift.exception.InternalServerExceptions;

public class DuplicatedUserException extends InternalServerException {
    private DuplicatedUserException(){ super(); }
    public DuplicatedUserException(String message){ super(message); }
}
