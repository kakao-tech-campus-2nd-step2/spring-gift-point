package gift.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class KakaoToken {

    private Long memberId;
    @JsonProperty("access_token")
    @SerializedName("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    @SerializedName("token_type")
    private String tokenType;
    @JsonProperty("refresh_token")
    @SerializedName("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    @SerializedName("expires_in")
    private int expiresIn;
    @JsonProperty("scope")
    @SerializedName("scope")
    private String scope;
    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    protected KakaoToken() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public int getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

}
