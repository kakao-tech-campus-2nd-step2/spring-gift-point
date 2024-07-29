package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.JSON_PROCESSING_ERROR;

public class JsonException extends RuntimeException{

    public JsonException(){
        super(JSON_PROCESSING_ERROR);
    }

    public JsonException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
