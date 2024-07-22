package gift.global.exception;

import gift.global.response.ErrorCode;

public class AccessTokenNotExistsException extends BusinessException {
    public AccessTokenNotExistsException() {
        super(ErrorCode.ACCESS_TOKEN_NOT_EXISTS_ERROR);
    }
}
