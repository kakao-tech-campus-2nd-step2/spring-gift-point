package gift.domain.exception.forbidden;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;

public abstract class ForbiddenException extends ServerException {

    public ForbiddenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
