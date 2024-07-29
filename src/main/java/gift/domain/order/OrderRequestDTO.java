package gift.domain.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDTO(

    @NotNull
    Long optionId,
    @NotNull
    @Min(1)
    Long quantity,
    @NotBlank
    String message
) {

}
