package gift.dto.response;

import gift.entity.Wish;

public record WishProductResponse(
        Long productId,
        String productName,
        int productPrice,
        String productImageUrl,
        int productAmount
) {
    public static WishProductResponse fromWish(Wish wish) {
        return new WishProductResponse(
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice(),
                wish.getProduct().getImageUrl(),
                wish.getQuantity()
        );
    }
}
