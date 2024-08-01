package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class TokenExpiredException extends BadRequestException {

    public TokenExpiredException() {
        super("토큰이 만료되었습니다. 재발급이 필요합니다.", ErrorCode.TOKEN_EXPIRED);
    }
}
