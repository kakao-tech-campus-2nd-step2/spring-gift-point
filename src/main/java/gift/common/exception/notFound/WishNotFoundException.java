package gift.common.exception.notFound;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WishNotFoundException extends CustomException {

    public WishNotFoundException() {
        super(ErrorCode.WISH_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

}
