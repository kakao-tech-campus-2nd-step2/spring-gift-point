package gift.domain.exception.unauthorized;

import gift.domain.exception.ErrorCode;

public class TokenExpiredException extends UnauthorizedException {

    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }
}
