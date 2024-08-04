package gift.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderRequestDto(
        @NotNull(message = "옵션을 선택하세요.")
        Long optionId,

        @NotNull(message = "옵션 개수를 선택하세요. (1 이상)")
        @Positive(message = "옵션 개수를 선택하세요. (1 이상)")
        Integer quantity,

        String message,

        @NotNull(message = "포인트는 0이상이어야 합니다.")
        @PositiveOrZero(message = "포인트는 0이상이어야 합니다.")
        Integer point
) {
}
