package gift.domain.exception.unauthorized;

import gift.domain.exception.ErrorCode;

public class TokenUnexpectedErrorException extends UnauthorizedException {

    public TokenUnexpectedErrorException() {
        super(ErrorCode.TOKEN_UNEXPECTED_ERROR);
    }
}
