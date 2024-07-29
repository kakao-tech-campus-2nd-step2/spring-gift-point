package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.CATEGORY_NAME_DUPLICATION;

public class NameDuplicationException extends RuntimeException{

    public NameDuplicationException(){
        super(CATEGORY_NAME_DUPLICATION);
    }

    public NameDuplicationException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
