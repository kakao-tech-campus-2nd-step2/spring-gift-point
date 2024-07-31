package gift.dto.response;

import gift.domain.Wish;

public record WishResponse(Long id, WishProductResponse product) {
    public static WishResponse from(final Wish wish){
        WishProductResponse product = new WishProductResponse(
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice(),
                wish.getProduct().getImageUrl());
        return new WishResponse(wish.getId(), product);
    }
}
