package gift.dto;

public class OauthTokenDTO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

    public OauthTokenDTO() {
    }

    public OauthTokenDTO(String access_token, String token_type, String refresh_token,
        int expires_in,
        String scope, int refresh_token_expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public int getExpiresIn() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public int getRefreshTokenExpiresIn() {
        return refresh_token_expires_in;
    }
}
