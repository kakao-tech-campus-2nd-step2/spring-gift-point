package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;

public abstract class NotFoundException extends ServerException {

    public NotFoundException(String object, ErrorCode errorCode) {
        super("The " + object + " was not found.", errorCode);
    }

    public NotFoundException(String customMessage, ErrorCode errorCode, Object dummy) {
        super(customMessage, errorCode);
    }
}
