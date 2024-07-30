package gift.common.exception;

import gift.wish.WishErrorCode;
import org.springframework.http.HttpStatus;

public class WishException extends RuntimeException {

    private final WishErrorCode wishErrorCode;
    private final HttpStatus httpStatus;

    public WishException(WishErrorCode wishErrorCode) {
        super(wishErrorCode.getMessage());
        this.wishErrorCode = wishErrorCode;
        this.httpStatus = wishErrorCode.getHttpStatus();
    }
}
