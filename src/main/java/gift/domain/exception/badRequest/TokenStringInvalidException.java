package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class TokenStringInvalidException extends BadRequestException {

    public TokenStringInvalidException() {
        super("토큰 문자열이 올바르지 않습니다.", ErrorCode.TOKEN_STRING_INVALID);
    }
}
