package gift.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequestDto(
    @NotNull(message = "옵션을 선택하세요.")
    Long optionId,

    @NotNull(message = "옵션 개수를 선택하세요. (1 이상)")
    @Positive(message = "옵션 개수를 선택하세요. (1 이상)")
    int quantity,

    String message
) {
}