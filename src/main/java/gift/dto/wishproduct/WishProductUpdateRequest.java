package gift.dto.wishproduct;

import jakarta.validation.constraints.PositiveOrZero;

public record WishProductUpdateRequest(
        @PositiveOrZero(message = "상품의 수량은 반드시 0개 이상이어야 합니다.")
        Integer quantity
) {
}
