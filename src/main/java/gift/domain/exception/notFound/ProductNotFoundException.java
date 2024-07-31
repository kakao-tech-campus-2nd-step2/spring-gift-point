package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

/**
 * 상품을 찾을 수 없을 때 발생하는 예외
 */
public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException() {
        super("product", ErrorCode.PRODUCT_NOT_FOUND);
    }
}
