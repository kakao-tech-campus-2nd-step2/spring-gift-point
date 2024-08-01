package gift.web.exception.duplicate;

public class MemberDuplicatedException extends DuplicatedException{
    public MemberDuplicatedException() {
        super("멤버가 이미 있슴다!");
    }
}
