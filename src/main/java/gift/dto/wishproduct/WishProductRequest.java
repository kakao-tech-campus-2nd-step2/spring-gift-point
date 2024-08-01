package gift.dto.wishproduct;

import jakarta.validation.constraints.NotNull;

public record WishProductRequest(
        @NotNull(message = "상품은 반드시 선택되어야 합니다.")
        Long productId
) {
}
