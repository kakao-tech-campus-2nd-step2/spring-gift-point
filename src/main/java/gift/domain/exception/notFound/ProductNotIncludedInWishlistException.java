package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

public class ProductNotIncludedInWishlistException extends NotFoundException {

    public ProductNotIncludedInWishlistException() {
        super("The product is not included your wishlist.", ErrorCode.PRODUCT_NOT_INCLUDED_IN_WISHLIST, null);
    }
}
