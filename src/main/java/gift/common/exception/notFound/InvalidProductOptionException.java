package gift.common.exception.notFound;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidProductOptionException extends CustomException {

    public InvalidProductOptionException() {
        super(ErrorCode.INVALID_PRODUCT_OPTION, HttpStatus.NOT_FOUND);
    }

}
