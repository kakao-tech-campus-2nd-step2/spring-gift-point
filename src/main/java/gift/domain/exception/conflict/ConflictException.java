package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;

public abstract class ConflictException extends ServerException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
