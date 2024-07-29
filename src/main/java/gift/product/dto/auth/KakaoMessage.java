package gift.product.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessage(
    @JsonProperty("object_type") String objectType,
    @JsonProperty("text") String text,
    @JsonProperty("link") Link link
) {

}
