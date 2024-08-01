package gift.wish.application.dto.response;

import gift.product.application.dto.response.ProductResponse;
import gift.wish.service.dto.WishInfo;

public record WishResponse(
        Long id,
        ProductResponse product
) {
    public static WishResponse from(WishInfo wishInfo) {
        ProductResponse productResponse = ProductResponse.from(wishInfo.product());
        return new WishResponse(wishInfo.id(), productResponse);
    }
}
