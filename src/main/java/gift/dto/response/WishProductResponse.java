package gift.dto.response;

import gift.entity.Wish;

public record WishProductResponse(
        Long wishId,
        Long productId,
        String productName,
        int productPrice,
        String productImageUrl) {
    public static WishProductResponse fromWish(Wish wish) {
        return new WishProductResponse(
                wish.getId(),
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice(),
                wish.getProduct().getImageUrl()
        );
    }
}
