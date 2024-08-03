package gift.DTO.wishlist;

import gift.domain.Product;
import gift.domain.Wishlist;

public record WishResponse(
    Long id,
    String name,
    int price,
    String imageUrl
) {

    public static WishResponse fromEntity(Wishlist wishEntity) {
        return new WishResponse(
            wishEntity.getId(),
            wishEntity.getProduct().getName(),
            wishEntity.getProduct().getPrice(),
            wishEntity.getProduct().getImageUrl()
        );
    }
}
