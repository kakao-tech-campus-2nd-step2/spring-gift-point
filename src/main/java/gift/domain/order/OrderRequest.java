package gift.domain.order;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull
    Long optionId,

    @NotNull
    Long quantity,

    String message
) {

}
