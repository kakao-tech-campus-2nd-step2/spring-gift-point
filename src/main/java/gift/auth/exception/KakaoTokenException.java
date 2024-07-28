package gift.auth.exception;

import gift.global.exception.BusinessException;
import gift.global.response.ErrorCode;

public class KakaoTokenException extends BusinessException {
    public KakaoTokenException() {
        super(ErrorCode.KAKAO_TOKEN_ERROR);
    }
}
