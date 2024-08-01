package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

public class ProductNotIncludedInWishlistException extends NotFoundException {

    public ProductNotIncludedInWishlistException() {
        super(ErrorCode.PRODUCT_NOT_INCLUDED_IN_WISHLIST);
    }
}
