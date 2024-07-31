package gift.dto;

import gift.model.Wishlist;

public class WishlistResponseDto {
    private Long id;
    private ProductResponseDto product;

    public WishlistResponseDto(Long id, ProductResponseDto product) {
        this.id = id;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public ProductResponseDto getProduct() {
        return product;
    }

    public static WishlistResponseDto fromEntity(Wishlist wishlist) {
        ProductResponseDto productResponseDto = new ProductResponseDto(
                wishlist.getProduct().getId(),
                wishlist.getProduct().getName(),
                wishlist.getProduct().getPrice(),
                wishlist.getProduct().getImageUrl()
        );
        return new WishlistResponseDto(wishlist.getId(), productResponseDto);
    }
}
