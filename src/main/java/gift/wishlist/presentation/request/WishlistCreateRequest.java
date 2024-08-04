package gift.wishlist.presentation.request;

import gift.wishlist.application.command.WishlistCreateCommand;

public record WishlistCreateRequest (
        Long productId
){
    public WishlistCreateCommand toCommand(Long memberId) {
        return new WishlistCreateCommand(
                memberId,
                productId
        );
    }
}
