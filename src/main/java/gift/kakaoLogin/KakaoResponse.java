package gift.kakaoLogin;

public class KakaoResponse {
    String access_token;
    String token_type;
    String refresh_token;
    Long expires_in;
    String scope;
    Long refresh_token_expires_in;

    public KakaoResponse() {
    }

    public KakaoResponse(String access_token, String token_type, String refresh_token, Long expires_in, String scope, Long refresh_token_expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public Long getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setRefresh_token_expires_in(Long refresh_token_expires_in) {
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
