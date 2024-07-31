package gift.product.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenDto(
    @JsonProperty("token") String accessToken
) {

}
