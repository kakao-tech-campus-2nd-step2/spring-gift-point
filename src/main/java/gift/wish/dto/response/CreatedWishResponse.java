package gift.wish.dto.response;

import gift.wish.entity.Wish;

public record CreatedWishResponse(
    Long id,
    Long productId
) {

    public static CreatedWishResponse from(Wish wish) {
        return new CreatedWishResponse(wish.getId(), wish.getProductId());
    }

}
