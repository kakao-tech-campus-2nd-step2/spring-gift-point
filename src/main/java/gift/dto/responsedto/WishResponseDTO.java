package gift.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.Wish;

public record WishResponseDTO(
    Long id,
    @JsonProperty("memeberDto")
    UserResponseDTO userResponseDTO,
    @JsonProperty("productDto")
    ProductResponseDTO productResponseDTO
) {
    public static WishResponseDTO from(Wish wish) {
        return new WishResponseDTO(
            wish.getId(),
            UserResponseDTO.from(wish.getUser()),
            ProductResponseDTO.from(wish.getProduct()));
    }
}
