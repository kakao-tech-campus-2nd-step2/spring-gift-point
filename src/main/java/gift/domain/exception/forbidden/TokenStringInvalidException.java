package gift.domain.exception.forbidden;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;

public class TokenStringInvalidException extends ServerException {

    public TokenStringInvalidException() {
        super("토큰 문자열이 올바르지 않습니다.", ErrorCode.TOKEN_STRING_INVALID);
    }
}
