package gift.dto.requestdto;

import jakarta.validation.constraints.NotNull;

public record WishRequestDTO(
    @NotNull
    Long userId,
    @NotNull
    Long productId) {
}
