package gift.common.exception.conflict;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class WishAlreadyExistsException extends CustomException {

    public WishAlreadyExistsException() {
        super(ErrorCode.PRODUCT_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

}
