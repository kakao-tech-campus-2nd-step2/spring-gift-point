package gift.dto.order;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull
        Long optionId,
        @NotNull
        @Min(value = 1, message = "수량은 1 이상의 정수로 입력해주세요.")
        Integer quantity,
        @Nullable
        String message,
        @NotNull
        @Min(value = 0, message = "포인트는 0 이상의 정수로 입력해주세요.")
        Integer point
) {
}
