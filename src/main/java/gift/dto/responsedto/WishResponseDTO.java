package gift.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.Wish;

public record WishResponseDTO(
    Long id,
    Long userId,
    @JsonProperty("productDto")
    ProductResponseDTO productResponseDTO
) {
    public static WishResponseDTO from(Wish wish) {
        return new WishResponseDTO(
            wish.getId(),
            wish.getUser().getId(),
            ProductResponseDTO.from(wish.getProduct()));
    }
}
