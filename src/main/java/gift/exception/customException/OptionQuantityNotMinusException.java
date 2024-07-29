package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.OPTION_QUANTITY_NOT_MINUS;

public class OptionQuantityNotMinusException extends RuntimeException{

    public OptionQuantityNotMinusException(){
        super(OPTION_QUANTITY_NOT_MINUS);
    }

    public OptionQuantityNotMinusException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
