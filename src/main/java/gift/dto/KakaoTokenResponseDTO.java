package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponseDTO {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "KakaoTokenResponseDTO{" +
            "accessToken='" + accessToken + '\'' +
            ", tokenType='" + tokenType + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", expiresIn=" + expiresIn +
            ", scope='" + scope + '\'' +
            '}';
    }
}
