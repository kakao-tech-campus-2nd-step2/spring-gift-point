package gift.product.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemainingPointResponse(
    @JsonProperty("remainingPoints") int point
) {

}
