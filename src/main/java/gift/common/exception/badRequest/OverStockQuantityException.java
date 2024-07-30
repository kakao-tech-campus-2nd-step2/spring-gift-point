package gift.common.exception.badRequest;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OverStockQuantityException extends CustomException {

    public OverStockQuantityException() {
        super(ErrorCode.OVER_STOCK_QUANTITY, HttpStatus.BAD_REQUEST);
    }

}
