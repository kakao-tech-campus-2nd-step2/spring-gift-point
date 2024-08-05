package gift.wishlist.presentation.response;

import gift.product.application.ProductServiceResponse;

public record WishlistReadProductResponse(
        Long id,
        String name,
        String price,
        String imageUrl
) {
    public static WishlistReadProductResponse from(ProductServiceResponse productServiceResponse) {
        return new WishlistReadProductResponse(
                productServiceResponse.id(),
                productServiceResponse.name(),
                productServiceResponse.price().toString(),
                productServiceResponse.imageUrl()
        );
    }
}
