package gift.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DataDto<T>(
        @JsonProperty("data") T data
) {
}
