package gift.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OauthTokenResponse(
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") Integer expiresIn,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("refresh_token_expires_in") String refreshTokenExpiresIn,
    @JsonProperty("scope") String scope
) {

}
