package gift.wishes.restapi.dto.response;

import gift.core.domain.product.Product;

public record WishResponse(
        Long productId,
        String name,
        int price,
        String imageUrl
) {
    public static WishResponse from(Product product) {
        return new WishResponse(
                product.id(),
                product.name(),
                product.price(),
                product.imageUrl()
        );
    }
}
