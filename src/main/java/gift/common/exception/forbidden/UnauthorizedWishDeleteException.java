package gift.common.exception.forbidden;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedWishDeleteException extends CustomException {

    public UnauthorizedWishDeleteException() {
        super(ErrorCode.UNAUTHORIZED_WISH_DELETION, HttpStatus.FORBIDDEN);
    }

}
