package gift.exception.memberException;

public class DuplicatedEmailException extends RuntimeException{
    public DuplicatedEmailException(String email){
        super(email + " : 중복된 이메일입니다.");
    }
}
