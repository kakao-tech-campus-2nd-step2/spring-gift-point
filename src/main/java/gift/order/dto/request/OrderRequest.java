package gift.order.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull
    Long optionId,

    @NotNull
    Integer quantity,

    @NotNull
    String message,

    @NotNull
    Boolean usePoint,

    Integer point
) {

}
