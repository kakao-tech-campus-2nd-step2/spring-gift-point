package gift.domain.exception.unauthorized;

import gift.domain.exception.ErrorCode;

public class TokenNotFoundException extends UnauthorizedException {

    public TokenNotFoundException() {
        super("토큰이 존재하지 않습니다. 발급이 필요합니다.", ErrorCode.TOKEN_NOT_FOUND);
    }
}
