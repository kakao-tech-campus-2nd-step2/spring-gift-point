package gift.common.exception.unauthorized;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenErrorException extends CustomException {

    public TokenErrorException() {
        super(ErrorCode.TOKEN_ERROR, HttpStatus.UNAUTHORIZED);
    }

}
