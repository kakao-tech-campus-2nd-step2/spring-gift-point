package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.MEMBER_NOT_FOUND;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(){
        super(MEMBER_NOT_FOUND);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
