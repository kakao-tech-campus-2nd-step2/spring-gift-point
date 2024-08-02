package gift.domain.exception.conflict;

import gift.domain.exception.ErrorCode;

public class ProductAlreadyExistsInWishlistException extends ConflictException {

    public ProductAlreadyExistsInWishlistException() {
        super(ErrorCode.PRODUCT_ALREADY_EXISTS_IN_WISHLIST);
    }
}
