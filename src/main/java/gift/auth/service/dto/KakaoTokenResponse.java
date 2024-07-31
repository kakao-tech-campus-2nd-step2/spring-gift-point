package gift.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.auth.token.OAuthAccessToken;
import gift.auth.token.OAuthRefreshToken;

public record KakaoTokenResponse(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("expires_in")
        Long expiresIn,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("refresh_token_expires_in")
        Long refreshTokenExpiresIn,

        @JsonProperty("scope")
        String scope
) {
    public String getAccessTokenWithTokenType() {
        return tokenType.concat(" ").concat(accessToken);
    }

    public OAuthAccessToken toAccessTokenFrom(String username) {
        return new OAuthAccessToken(username, tokenType, accessToken, "kakao", expiresIn);
    }

    public OAuthRefreshToken toRefreshTokenFrom(String username) {
        return new OAuthRefreshToken(username, tokenType, refreshToken, "kakao", refreshTokenExpiresIn);
    }
}