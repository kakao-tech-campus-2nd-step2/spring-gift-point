package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.EMAIL_DUPLICATION;

public class EmailDuplicationException extends RuntimeException{

    public EmailDuplicationException(){
        super(EMAIL_DUPLICATION);
    }

    public EmailDuplicationException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
