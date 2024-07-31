package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class ProductOptionsEmptyException extends BadRequestException {

    public ProductOptionsEmptyException() {
        super("A product must have at least one option.", ErrorCode.PRODUCT_OPTIONS_EMPTY);
    }
}
