package gift.domain.cartItem.dto;

import gift.domain.cartItem.CartItem;

public record CartItemDTO(
    Long id,
    Long productId,
    String name,
    Integer price,
    String imageUrl,
    Integer count
) {

    public CartItemDTO(CartItem cartItem) {
        this(
            cartItem.getId(),
            cartItem.getProduct().getId(),
            cartItem.getProduct().getName(),
            cartItem.getProduct().getPrice(),
            cartItem.getProduct().getImageUrl(),
            cartItem.getCount()
        );
    }
}
