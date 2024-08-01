package gift.wish.dto.response;


import gift.wish.entity.Wish;

public record WishResponse(
    Long id,
    Long productId
) {

    public static WishResponse from(Wish wish) {
        return new WishResponse(wish.getId(), wish.getProductId());
    }

}
