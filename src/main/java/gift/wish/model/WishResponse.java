package gift.wish.model;

public record WishResponse(Long productId, Integer count) {

    public static WishResponse from(Wish wish) {
        return new WishResponse(
            wish.getProduct().getId(),
            wish.getCount()
        );
    }
}
