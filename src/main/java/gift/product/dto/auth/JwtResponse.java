package gift.product.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("refresh_token") String refreshToken
) {

}
