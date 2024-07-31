package gift.domain.exception.forbidden;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;

public class TokenExpiredException extends ServerException {

    public TokenExpiredException() {
        super("토큰이 만료되었습니다. 재발급이 필요합니다.", ErrorCode.TOKEN_EXPIRED);
    }
}
