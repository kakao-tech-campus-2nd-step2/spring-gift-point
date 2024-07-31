package gift.product.dto.wish;

import gift.product.dto.product.ProductInfoResponse;

public record WishResponse(
    Long id,
    ProductInfoResponse product
) {

}
