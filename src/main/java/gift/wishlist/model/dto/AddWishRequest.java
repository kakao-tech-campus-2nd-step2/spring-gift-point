package gift.wishlist.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddWishRequest(
        @NotNull
        Long productId,

        @NotNull
        @Min(value = 1, message = "상품 수량은 1 이상의 정수로 입력해주세요.")
        int quantity
) {
}
