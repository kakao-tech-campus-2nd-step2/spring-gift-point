package gift.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("Member not found");
    }

}
