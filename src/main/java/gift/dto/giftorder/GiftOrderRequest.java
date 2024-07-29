package gift.dto.giftorder;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GiftOrderRequest(
        @NotNull(message = "상품 옵션은 반드시 선택되어야 합니다.")
        Long optionId,
        @Min(value = 1, message = "수량은 최소 1개 이상, 1억개 미만입니다.")
        @Max(value = 100_000_000, message = "수량은 최소 1개 이상, 1억개 미만입니다.")
        Integer quantity,
        @NotBlank(message = "메시지의 길이는 최소 1자 이상이어야 합니다.")
        String message
) {
}
