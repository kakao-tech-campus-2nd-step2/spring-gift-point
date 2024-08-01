package gift.domain.exception.unauthorized;

import gift.domain.exception.ErrorCode;

public class TokenStringInvalidException extends UnauthorizedException {

    public TokenStringInvalidException() {
        super(ErrorCode.TOKEN_STRING_INVALID);
    }
}
