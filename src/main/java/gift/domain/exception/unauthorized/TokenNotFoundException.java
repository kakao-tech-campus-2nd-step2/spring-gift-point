package gift.domain.exception.unauthorized;

import gift.domain.exception.ErrorCode;

public class TokenNotFoundException extends UnauthorizedException {

    public TokenNotFoundException() {
        super(ErrorCode.TOKEN_NOT_FOUND);
    }
}
