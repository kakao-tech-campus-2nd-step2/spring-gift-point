package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class ProductOptionsEmptyException extends BadRequestException {

    public ProductOptionsEmptyException() {
        super(ErrorCode.PRODUCT_OPTIONS_EMPTY);
    }
}
