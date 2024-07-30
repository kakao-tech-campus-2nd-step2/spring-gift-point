package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponse(
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("scope")
        String scope,
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("expires_in")
        Long expiresIn,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("refresh_token_expires_in")
        Long refreshTokenExpiresIn
) {
}
