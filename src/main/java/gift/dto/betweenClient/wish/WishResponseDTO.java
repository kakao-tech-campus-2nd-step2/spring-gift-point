package gift.dto.betweenClient.wish;

import gift.dto.betweenClient.product.ProductResponseDTO;
import gift.entity.Wish;
import java.time.LocalDateTime;

public record WishResponseDTO(Long id, String name, Integer price, String imageUrl, Integer quantity, LocalDateTime createdDate) {
    public static WishResponseDTO convertToWishDTO(Wish wish) {

        ProductResponseDTO productResponseDTO = ProductResponseDTO.convertToProductResponseDTO(wish.getProduct());

        return new WishResponseDTO(productResponseDTO.id(), productResponseDTO.name(), productResponseDTO.price(), productResponseDTO.imageUrl(),
                wish.getQuantity(), wish.getCreatedDate());
    }
}