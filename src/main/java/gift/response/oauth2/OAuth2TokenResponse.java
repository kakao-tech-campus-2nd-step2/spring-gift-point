package gift.response.oauth2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuth2TokenResponse(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("refresh_token")
    String refreshToken,
    @JsonProperty("expires_in")
    Integer expiresIn,
    @JsonProperty("refresh_token_expires_in")
    Integer refreshTokenExpiresIn,
    String scope
) {

    @JsonCreator
    public OAuth2TokenResponse(String accessToken,
        String tokenType,
        String refreshToken,
        Integer expiresIn,
        Integer refreshTokenExpiresIn,
        String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.scope = scope;
    }
}