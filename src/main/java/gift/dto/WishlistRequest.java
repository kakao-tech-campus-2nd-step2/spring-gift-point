package gift.dto;

import jakarta.validation.constraints.NotNull;

public record WishlistRequest(
    @NotNull(message = "상품 ID가 필요합니다.")
    Long productId
) {

}