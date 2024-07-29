package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.FORBIDDEN_MESSAGE;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(){
        super(FORBIDDEN_MESSAGE);
    }
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
