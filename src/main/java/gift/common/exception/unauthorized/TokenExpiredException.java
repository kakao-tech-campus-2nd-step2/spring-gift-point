package gift.common.exception.unauthorized;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends CustomException {

    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
    }

}
