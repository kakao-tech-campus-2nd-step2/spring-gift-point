package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishRequest(
        @Min(value = 1, message = "상품 id는 1 이상이어야 합니다.")
        @NotNull
        Long productId
) {
}
