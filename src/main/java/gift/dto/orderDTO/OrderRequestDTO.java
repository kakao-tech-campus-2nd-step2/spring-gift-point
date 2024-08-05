package gift.dto.orderDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDTO(
    @NotNull(message = "옵션 ID는 필수입니다")
    Long optionId,

    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    @NotNull(message = "수량은 필수입니다.")
    Long quantity,

    String message,

    String accessToken
) {

}