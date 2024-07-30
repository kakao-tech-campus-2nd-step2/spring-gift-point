package gift.dto.betweenClient.wish;

import gift.dto.betweenClient.product.ProductResponseDTO;
import gift.entity.Wish;

public record WishResponseDTO(ProductResponseDTO productResponseDTO, Integer quantity) {
    public static WishResponseDTO convertToWishDTO(Wish wish) {
        return new WishResponseDTO(ProductResponseDTO.convertToProductResponseDTO(wish.getProduct()), wish.getQuantity());
    }
}