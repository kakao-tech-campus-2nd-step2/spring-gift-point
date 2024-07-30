package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponse {

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String accessToken;
        private String tokenType;
        private String refreshToken;
        private int expiresIn;
        private String scope;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder expiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }

        public KakaoTokenResponse build() {
            KakaoTokenResponse response = new KakaoTokenResponse();
            response.accessToken = this.accessToken;
            response.tokenType = this.tokenType;
            response.refreshToken = this.refreshToken;
            response.expiresIn = this.expiresIn;
            response.scope = this.scope;
            return response;
        }
    }
}
