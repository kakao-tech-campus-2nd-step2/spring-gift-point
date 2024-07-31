package gift.product.dto.wish;

import gift.product.dto.product.ProductInfoForWishResponse;

public record WishResponse(
    Long id,
    ProductInfoForWishResponse product
) {

}
