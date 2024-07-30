package gift.common.exception.unauthorized;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenNotFoundException extends CustomException {

    public TokenNotFoundException() {
        super(ErrorCode.TOKEN_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }

}
