package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;

public abstract class BadRequestException extends ServerException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
