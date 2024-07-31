package gift.doamin.user.dto;

public class KakaoOAuthTokenResponse {

    private String token_type;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;

    public KakaoOAuthTokenResponse(String token_type, String access_token, String expires_in,
        String refresh_token, String refresh_token_expires_in) {
        this.token_type = token_type;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getExpiresIn() {
        return expires_in;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public String getRefreshTokenExpiresIn() {
        return refresh_token_expires_in;
    }
}
