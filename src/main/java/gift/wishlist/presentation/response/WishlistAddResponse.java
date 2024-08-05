package gift.wishlist.presentation.response;

public record WishlistAddResponse(
        Long id,
        Long memberId,
        Long productId
) {
    public static WishlistAddResponse of(Long id, Long memberId, Long productId) {
        return new WishlistAddResponse(id, memberId, productId);
    }
}
