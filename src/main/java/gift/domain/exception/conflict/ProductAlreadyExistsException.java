package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

/**
 * 상품이 이미 존재할 때 발생하는 예외
 */
public class ProductAlreadyExistsException extends ConflictException {

    public ProductAlreadyExistsException() {
        super("The product already exists.", ErrorCode.PRODUCT_ALREADY_EXISTS);
    }
}
