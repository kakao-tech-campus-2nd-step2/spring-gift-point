package gift.exception;

public class MemberAlreadyExistsException extends ConflictException {

    public MemberAlreadyExistsException() {
        super("Member already exists");
    }
}
