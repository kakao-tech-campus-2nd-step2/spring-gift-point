package gift.dto.wishproduct;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WishProductAddRequest(
        @NotNull(message = "상품은 반드시 선택되어야 합니다.")
        Long productId,
        @Positive(message = "상품의 수량은 반드시 1개 이상이어야 합니다.")
        Integer quantity
) {
}
