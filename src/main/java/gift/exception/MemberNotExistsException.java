package gift.exception;

public class MemberNotExistsException extends NotFoundException {

    public MemberNotExistsException() {
        super("Member does not Exist");
    }
}