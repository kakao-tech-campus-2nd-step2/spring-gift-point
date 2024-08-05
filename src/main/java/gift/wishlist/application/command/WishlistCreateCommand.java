package gift.wishlist.application.command;

public record WishlistCreateCommand(
        Long memberId,
        Long productId
) {
}
