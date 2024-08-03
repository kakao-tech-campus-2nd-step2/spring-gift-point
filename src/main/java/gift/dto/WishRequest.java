package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishRequest(
        @NotNull
        @Min(1)
        @JsonProperty("product_id")
        Long productId
) {}
