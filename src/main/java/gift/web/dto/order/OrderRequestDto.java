package gift.web.dto.order;

import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
    @NotNull
    Long optionId,

    @NotNull
    Long quantity,

    @NotNull
    String message
) {

}
