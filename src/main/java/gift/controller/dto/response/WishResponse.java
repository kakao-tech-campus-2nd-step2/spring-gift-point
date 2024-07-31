package gift.controller.dto.response;

import gift.model.Wish;

public record WishResponse(
        Long wishId,
        Long productId,
        String productName,
        int price,
        String imageUrl
) {
    public static WishResponse from(Wish wish) {
        return new WishResponse(
                wish.getId(),
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice(),
                wish.getProduct().getImageUrl());
    }
}
