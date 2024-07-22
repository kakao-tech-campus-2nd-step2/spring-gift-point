package gift.product.exception;

import gift.global.exception.BusinessException;
import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorCode;

public class ProductValidException extends DomainValidationException {
    public ProductValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
