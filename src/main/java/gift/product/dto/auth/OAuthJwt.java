package gift.product.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthJwt(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("refresh_token") String refreshToken
) {

}
