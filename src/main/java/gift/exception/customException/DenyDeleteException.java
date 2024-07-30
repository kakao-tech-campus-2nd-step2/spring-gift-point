package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.DENY_OPTION_DELETE;

public class DenyDeleteException extends RuntimeException{

    public DenyDeleteException(){
        super(DENY_OPTION_DELETE);
    }

    public DenyDeleteException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
