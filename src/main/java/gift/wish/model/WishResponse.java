package gift.wish.model;

public record WishResponse(Long id, Long productId) {

    public static WishResponse from(Wish wish) {
        return new WishResponse(
            wish.getId(),
            wish.getProduct().getId()
        );
    }
}
