package gift.domain.exception.unauthorized;

import gift.domain.exception.ErrorCode;

public class TokenUnexpectedErrorException extends UnauthorizedException {

    public TokenUnexpectedErrorException() {
        super("알 수 없는 오류가 발생했습니다. 토큰 재발급이 필요합니다.", ErrorCode.TOKEN_UNEXPECTED_ERROR);
    }
}
