package gift.dto.response;

import gift.domain.WishlistItem;

public class WishlistItemResponse {
    private Long id;
    private ProductResponse product;

    public WishlistItemResponse(Long id, ProductResponse product) {
        this.id = id;
        this.product = product;
    }

    public static WishlistItemResponse fromWishlistItem(WishlistItem wishlistItem) {
        return new WishlistItemResponse(wishlistItem.getId(), ProductResponse.fromProduct(wishlistItem.getProduct()));
    }

}
