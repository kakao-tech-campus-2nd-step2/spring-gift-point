package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.UNAUTHORIZATION_EXCEPTION;

public class UnAuthorizationException extends RuntimeException{

    public UnAuthorizationException(){
        super(UNAUTHORIZATION_EXCEPTION);
    }

    public UnAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
