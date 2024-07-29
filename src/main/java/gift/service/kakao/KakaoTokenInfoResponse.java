package gift.service.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenInfoResponse {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Integer expiresIn;
    private String scope;
    private Integer refreshTokenExpiresIn;

    public KakaoTokenInfoResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public KakaoTokenInfoResponse(@JsonProperty("access_token") String accessToken,
                                  @JsonProperty("token_type") String tokenType,
                                  @JsonProperty("refresh_token") String refreshToken,
                                  @JsonProperty("expires_in") Integer expiresIn,
                                  @JsonProperty("scope") String scope,
                                  @JsonProperty("refresh_token_expires_in") Integer refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
