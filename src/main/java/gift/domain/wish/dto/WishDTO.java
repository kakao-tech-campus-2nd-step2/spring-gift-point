package gift.domain.wish.dto;

import gift.domain.wish.Wish;

public record WishDTO(
    Long id,
    Long productId,
    String name,
    Integer price,
    String imageUrl,
    Integer count
) {

    public WishDTO(Wish wish) {
        this(
            wish.getId(),
            wish.getProduct().getId(),
            wish.getProduct().getName(),
            wish.getProduct().getPrice(),
            wish.getProduct().getImageUrl(),
            wish.getCount()
        );
    }
}
