package gift.product.dto;

import jakarta.validation.constraints.NotNull;

public record WishOptionRequest(
        @NotNull
        Long optionId
) { }
